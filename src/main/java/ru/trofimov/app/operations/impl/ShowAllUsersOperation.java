package ru.trofimov.app.operations.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.trofimov.app.operations.Operation;
import ru.trofimov.app.service.UserService;

import java.util.Scanner;

@Component
public class ShowAllUsersOperation implements Operation {

    private final UserService userService;

    @Autowired
    public ShowAllUsersOperation(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.println("List of all users: ");
        userService.getAllUsers().forEach(System.out::println);
    }
}
