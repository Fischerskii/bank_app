package ru.trofimov.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.trofimov.app.entity.Account;
import ru.trofimov.app.entity.User;
import ru.trofimov.app.service.AccountService;
import ru.trofimov.app.service.UserService;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Component
public class AccountServiceImpl implements AccountService {

    private int lastIndexId;
    private final UserService userService;
    private final Map<Integer, Account> accounts = new HashMap<>();

    @Value("${account.transfer-commission}")
    double transferCommission;

    @Autowired
    public AccountServiceImpl(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Override
    public User createAccount(int userId) {
        Account account = new Account(lastIndexId, userId);
        accounts.put(lastIndexId, account);
        userService.addAccountForUser(userId, lastIndexId);

        User user = userService.getUserById(userId);
        lastIndexId++;
        return user;
    }

    @Override
    public void removeAccount(int accountId) throws AccountNotFoundException {
        if (accounts.containsKey(accountId)) {
            accounts.remove(accountId);
        } else {
            throw new AccountNotFoundException("Account with id: " + accountId + " not found");
        }
        accounts.remove(accountId);
        userService.deleteAccountId(accountId);
    }

    @Override
    public void deposit(int accountId, BigDecimal amount) {
        if (amount.compareTo(new BigDecimal("0")) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        if (accounts.containsKey(accountId)) {
            Account account = accounts.get(accountId);
            BigDecimal currentMoneyAmount = account.getMoneyAmount();
            account.setMoneyAmount(currentMoneyAmount.add(amount));
        }
    }

    @Override
    public void transfer(int sourceAccountId, int targetAccountId, BigDecimal amount) {
        if (amount.compareTo(new BigDecimal("0")) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        if (accounts.containsKey(sourceAccountId) && accounts.containsKey(targetAccountId)) {
            Account sourceAccount = accounts.get(sourceAccountId);
            Account targetAccount = accounts.get(targetAccountId);
            BigDecimal currentMoneyAmount = sourceAccount.getMoneyAmount();
            BigDecimal targetMoneyAmount = targetAccount.getMoneyAmount();

            BigDecimal sourceAccountTotalSum = currentMoneyAmount.subtract(amount);

            if (sourceAccount.getUserId() != targetAccount.getUserId()) {
                BigDecimal totalSumWithCommission = sourceAccountTotalSum
                        .multiply(new BigDecimal(String.valueOf(transferCommission)))
                        .divide(new BigDecimal("100"), RoundingMode.HALF_UP);
                sourceAccount.setMoneyAmount(totalSumWithCommission);
            } else {
                sourceAccount.setMoneyAmount(sourceAccountTotalSum);
            }

            targetAccount.setMoneyAmount(targetMoneyAmount.add(amount));
        }
    }

    @Override
    public void withdraw(int accountId, BigDecimal amount) {
        if (amount.compareTo(new BigDecimal("0")) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        if (accounts.containsKey(accountId)) {
            Account account = accounts.get(accountId);
            BigDecimal currentMoneyAmount = account.getMoneyAmount();

            BigDecimal totalSum = currentMoneyAmount.subtract(amount);

            if (totalSum.compareTo(new BigDecimal("0")) < 0) {
                throw new IllegalArgumentException("Error: insufficient funds in the account with id: "
                        + accountId + ". " + "Available amount for write-off: " + currentMoneyAmount);
            } else {
                account.setMoneyAmount(currentMoneyAmount.subtract(amount));
            }
        }
    }
}
