package ru.trofimov.app.operations.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.trofimov.app.operations.Operation;
import ru.trofimov.app.service.AccountService;

import java.math.BigDecimal;
import java.util.Scanner;

@Component
public class AccountDepositOperation implements Operation {

    private final AccountService accountService;

    @Autowired
    public AccountDepositOperation(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Enter account ID: ");

        int accountId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter amount to deposit: ");
        String amount = scanner.nextLine();

        accountService.deposit(accountId, new BigDecimal(amount));
    }
}
