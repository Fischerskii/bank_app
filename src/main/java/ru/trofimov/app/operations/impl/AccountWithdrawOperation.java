package ru.trofimov.app.operations.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.trofimov.app.operations.Operation;
import ru.trofimov.app.service.AccountService;

import java.math.BigDecimal;
import java.util.Scanner;

@Component
public class AccountWithdrawOperation implements Operation {

    private final AccountService accountService;

    @Autowired
    public AccountWithdrawOperation(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Enter account ID to withdraw from: ");
        Long accountId = scanner.nextLong();
        scanner.nextLine();

        System.out.println("Enter amount to withdraw: ");
        String amount = scanner.nextLine();

        accountService.withdraw(accountId, new BigDecimal(amount));
        System.out.printf("%s was successfully written off from account %d %n", amount, accountId);
    }
}
