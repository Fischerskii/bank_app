package ru.trofimov.app.operations.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.trofimov.app.entity.User;
import ru.trofimov.app.operations.Operation;
import ru.trofimov.app.service.UserService;

import java.util.Scanner;

@Component
public class UserCreateOperation implements Operation {

    private final UserService userService;

    @Autowired
    public UserCreateOperation(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute(Scanner scanner) throws IllegalArgumentException {
        System.out.println("Enter login for new user: ");
            User user = userService.createUser(scanner.nextLine());
            System.out.println("User created: " + user);
    }
}
