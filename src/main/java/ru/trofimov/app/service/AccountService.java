package ru.trofimov.app.service;

import ru.trofimov.app.entity.Account;
import ru.trofimov.app.entity.User;
import ru.trofimov.app.exceptions.UserNotFoundException;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    User createAccount(int userId);

    void removeAccount(int accountId) throws AccountNotFoundException;

    Account getAccount(int accountId) throws AccountNotFoundException;

    List<Account> getAllUserAccounts(int userId) throws UserNotFoundException;

    void deposit(int accountId, BigDecimal amount);

    void transfer(int sourceAccountId, int targetAccountId, BigDecimal amount);

    void withdraw(int accountId, BigDecimal amount);
}
