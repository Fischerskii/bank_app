package ru.trofimov.app.operations.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.trofimov.app.entity.Account;
import ru.trofimov.app.operations.Operation;
import ru.trofimov.app.service.AccountService;

import java.util.List;
import java.util.Scanner;

@Component
public class GetAllUserAccounts implements Operation {

    private final AccountService accountService;

    @Autowired
    public GetAllUserAccounts(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Enter userId to get all user accounts: ");

        int userId = scanner.nextInt();
        scanner.nextLine();

        List<Account> allUserAccounts = accountService.getAllUserAccounts(userId);
        allUserAccounts.forEach(System.out::println);
    }
}
