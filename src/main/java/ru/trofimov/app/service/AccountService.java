package ru.trofimov.app.service;

import ru.trofimov.app.entity.User;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;

public interface AccountService {

    User createAccount(int userId);

    void removeAccount(int accountId) throws AccountNotFoundException;

    void deposit(int accountId, BigDecimal amount);

    void transfer(int sourceAccountId, int targetAccountId, BigDecimal amount);

    void withdraw(int accountId, BigDecimal amount);
}
