package ru.trofimov.app.operations.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.trofimov.app.operations.Operation;
import ru.trofimov.app.service.AccountService;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Scanner;

@Component
public class AccountCloseOperation implements Operation {

    private final AccountService accountService;

    @Autowired
    public AccountCloseOperation(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Enter account ID to close:");

        int accountId = scanner.nextInt();
        scanner.nextLine();

        try {
            accountService.removeAccount(accountId);
            System.out.println("Account with ID " + accountId + " has been closed.");
        } catch (AccountNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
