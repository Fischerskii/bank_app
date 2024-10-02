package ru.trofimov.app.operations.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.trofimov.app.entity.Account;
import ru.trofimov.app.operations.Operation;
import ru.trofimov.app.service.AccountService;

import java.util.Scanner;

@Component
public class AccountCreateOperation implements Operation {

    private final AccountService accountService;

    @Autowired
    public AccountCreateOperation(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Enter the user id for which to create an account: ");

        Long userId = scanner.nextLong();
        scanner.nextLine();
        Account account = accountService.createAccount(userId);
        System.out.println("New account created with ID: " + userId + " for user: " + account.getUser().getLogin());
    }
}
