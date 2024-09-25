package ru.trofimov.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.trofimov.app.entity.Account;
import ru.trofimov.app.entity.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AccountService {

    private int lastIndexId;
    private final UserService userService;
    private final Map<Integer, Account> accounts = new HashMap<>();

    @Value("${account.default-amount}")
    private BigDecimal defaultMoneyAmount;

    @Value("${account.transfer-commission}")
    double transferCommission;

    @Autowired
    public AccountService(@Lazy UserService userService) {
        this.userService = userService;
    }

    public User createAccount(int userId) {
        Account account = new Account(lastIndexId, userId, defaultMoneyAmount);
        accounts.put(lastIndexId, account);
        userService.addAccountForUser(userId, lastIndexId);

        User user = userService.getUserById(userId);
        lastIndexId++;
        return user;
    }

    public void removeAccount(int accountId) throws IllegalArgumentException {
        if (accounts.containsKey(accountId)) {
            accounts.remove(accountId);
        } else {
            throw new IllegalArgumentException("Account with id: " + accountId + " not found");
        }
        accounts.remove(accountId);
        userService.deleteAccountId(accountId);
    }

    public Account getAccount(int accountId) {
        if (accounts.containsKey(accountId)) {
            return accounts.get(accountId);
        } else {
            throw new IllegalArgumentException("Account with id: " + accountId + " not found");
        }
    }

    public List<Account> getAllUserAccounts(int userId) throws IllegalArgumentException{
        User user;
        List<Account> userAccounts = new ArrayList<>();
        if (accounts.containsKey(userId)) {
            user = userService.getUserById(userId);
        } else {
            throw new IllegalArgumentException("User with id: " + userId + " not found");
        }
        List<Integer> accountIds = user.getAccountIds();
        accountIds.forEach(accountId -> userAccounts.add(accounts.get(accountId)));
        return userAccounts;
    }

    public void deposit(int accountId, BigDecimal amount) throws IllegalArgumentException {
        if (amount.compareTo(new BigDecimal("0")) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        if (accounts.containsKey(accountId)) {
            Account account = accounts.get(accountId);
            BigDecimal currentMoneyAmount = account.getMoneyAmount();
            account.setMoneyAmount(currentMoneyAmount.add(amount));
        }
    }

    public void transfer(int sourceAccountId, int targetAccountId, BigDecimal amount) throws IllegalArgumentException {
        if (amount.compareTo(new BigDecimal("0")) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        Account sourceAccount = accounts.get(sourceAccountId);
        BigDecimal currentMoneyAmount = sourceAccount.getMoneyAmount();

        if (currentMoneyAmount.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Amount must be less or equal than source account sum. " +
                    "Source account sum = " + currentMoneyAmount);
        }

        if (accounts.containsKey(sourceAccountId) && accounts.containsKey(targetAccountId)) {
            Account targetAccount = accounts.get(targetAccountId);
            BigDecimal targetMoneyAmount = targetAccount.getMoneyAmount();

            BigDecimal sourceAccountTotalSum = currentMoneyAmount.subtract(amount);

            if (sourceAccount.getUserId() != targetAccount.getUserId()) {
                BigDecimal commissionSum = amount
                        .multiply(new BigDecimal(String.valueOf(transferCommission)))
                        .divide(new BigDecimal("100"), RoundingMode.HALF_UP);
                sourceAccount.setMoneyAmount(
                        sourceAccountTotalSum.subtract(commissionSum));
            } else {
                sourceAccount.setMoneyAmount(sourceAccountTotalSum);
            }

            targetAccount.setMoneyAmount(targetMoneyAmount.add(amount));
        }
    }

    public void withdraw(int accountId, BigDecimal amount) throws IllegalArgumentException {
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
