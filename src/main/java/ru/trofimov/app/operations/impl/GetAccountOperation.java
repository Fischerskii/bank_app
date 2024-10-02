package ru.trofimov.app.operations.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.trofimov.app.entity.Account;
import ru.trofimov.app.operations.Operation;
import ru.trofimov.app.service.AccountService;

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
        Long accountId = scanner.nextLong();
        scanner.nextLine();

        Account account = accountService.getAccount(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account with id %d not found"
                        .formatted(accountId)));

        System.out.println("Account information: " + account);
    }
}
