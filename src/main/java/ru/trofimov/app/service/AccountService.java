package ru.trofimov.app.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.trofimov.app.entity.Account;
import ru.trofimov.app.entity.User;
import ru.trofimov.app.helper.TransactionHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final TransactionHelper transactionHelper;
    private final SessionFactory sessionFactory;
    private final UserService userService;
    private final AccountProperties accountProperties;

    @Autowired
    public AccountService(TransactionHelper transactionHelper,
                          SessionFactory sessionFactory,
                          UserService userService,
                          AccountProperties accountProperties) {
        this.transactionHelper = transactionHelper;
        this.sessionFactory = sessionFactory;
        this.userService = userService;
        this.accountProperties = accountProperties;
    }

    public Account createAccount(Long userId) {
        return transactionHelper.executeInTransaction(session -> {
            User user = userService.getUserById(userId);
            Account account = new Account(user, accountProperties.getDefaultMoneyAmount());
            session.persist(account);
            return account;
        });
    }

    public Account createAccountForNewUser(User user, Session session) {
        Account account = new Account(user, accountProperties.getDefaultMoneyAmount());
        session.persist(account);
        return account;
    }

    public void removeAccount(Long accountId) {
        transactionHelper.executeInTransaction(session -> {
            Account account = session.get(Account.class, accountId);
            session.remove(account);
        });
    }

    public Optional<Account> getAccount(Long accountId) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Account.class, accountId));
        }
    }

    public List<Account> getAllUserAccounts(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT a FROM Account a WHERE a.user.id = :userId", Account.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }

    public void deposit(Long accountId, BigDecimal amount) {
        transactionHelper.executeInTransaction(session -> {
            Account account = getAccount(accountId)
                    .orElseThrow(() -> new IllegalArgumentException("Account with id %d not found"
                            .formatted(accountId)));
            account.setMoneyAmount(account.getMoneyAmount().add(amount));
            session.merge(account);
        });
    }

    public void transfer(Long sourceAccountId, Long targetAccountId, BigDecimal amount) throws IllegalArgumentException {
        transactionHelper.executeInTransaction(session -> {
            Account sourceAccount = getAccount(sourceAccountId).
                    orElseThrow(() -> new IllegalArgumentException("Account with id %d not found"
                            .formatted(sourceAccountId)));
            Account targetAccount = getAccount(targetAccountId)
                    .orElseThrow(() -> new IllegalArgumentException("Account with id %d not found"
                            .formatted(targetAccountId)));

            if (sourceAccount.getMoneyAmount().compareTo(amount) < 0) {
                throw new IllegalArgumentException("Amount must be greater than 0");
            }

            sourceAccount.setMoneyAmount(sourceAccount.getMoneyAmount().subtract(amount));
            targetAccount.setMoneyAmount(targetAccount.getMoneyAmount().add(amount));

            if (sourceAccount.getUser() != targetAccount.getUser()) {
                BigDecimal commissionSum = amount
                        .multiply(new BigDecimal(String.valueOf(accountProperties.getTransferCommission())))
                        .divide(new BigDecimal("100"), RoundingMode.HALF_UP);
                sourceAccount.setMoneyAmount(sourceAccount.getMoneyAmount().subtract(commissionSum));
            }

            session.merge(sourceAccount);
            session.merge(targetAccount);
        });
    }

    public void withdraw(Long accountId, BigDecimal amount) throws IllegalArgumentException {
        transactionHelper.executeInTransaction(session -> {
            Account account = getAccount(accountId)
                    .orElseThrow(() -> new IllegalArgumentException("Account with id %d not found"
                            .formatted(accountId)));
            BigDecimal moneyAmount = account.getMoneyAmount();
            if (moneyAmount.compareTo(amount) < 0) {
                throw new IllegalArgumentException("Error executing command ACCOUNT_WITHDRAW: error=No such money to withdraw\n" +
                        "/from account: id=%d, moneyAmount=%s, attemptedWithdraw=%s"
                                .formatted(accountId, moneyAmount, amount));
            }

            account.setMoneyAmount(account.getMoneyAmount().subtract(amount));
            session.merge(account);
        });
    }
}
