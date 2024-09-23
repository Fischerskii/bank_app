package ru.trofimov.app.operations.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.trofimov.app.entity.Account;
import ru.trofimov.app.operations.Operation;
import ru.trofimov.app.service.AccountService;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Scanner;

@Component
public class GetAccountOperation implements Operation {
    private final AccountService accountService;

    @Autowired
    public GetAccountOperation(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Enter account ID to get account information: ");
        int accountId = scanner.nextInt();
        scanner.nextLine();

        try {
            Account account = accountService.getAccount(accountId);
            System.out.println("Account information: " + account);
        } catch (AccountNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
