package ru.trofimov.app.operations.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.trofimov.app.operations.Operation;
import ru.trofimov.app.service.AccountService;

import java.math.BigDecimal;
import java.util.Scanner;

@Component
public class AccountTransferOperation implements Operation {

    private final AccountService accountService;

    @Autowired
    public AccountTransferOperation(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Enter source account ID: ");
        int sourceAccountId = scanner.nextInt();

        System.out.println("Enter target account ID: ");
        int targetAccountId = scanner.nextInt();

        System.out.println("Enter amount to transfer: ");
        String amount = scanner.nextLine();

        accountService.transfer(sourceAccountId, targetAccountId, new BigDecimal(amount));
    }
}
