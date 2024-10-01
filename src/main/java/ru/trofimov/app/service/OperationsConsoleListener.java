package ru.trofimov.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.trofimov.app.enums.OperationTypes;
import ru.trofimov.app.operations.Operation;
import ru.trofimov.app.operations.OperationFactory;

import java.util.Arrays;
import java.util.Scanner;

@Component
public class OperationsConsoleListener implements Runnable {

    private final OperationFactory operationFactory;

    @Autowired
    public OperationsConsoleListener(OperationFactory operationFactory) {
        this.operationFactory = operationFactory;
    }

    public void mainMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Please enter one of operation type:");
            Arrays.stream(OperationTypes.values())
                    .forEach(s -> System.out.println("-" + s));

            String inputOperationType = scanner.nextLine().toUpperCase();
            if (inputOperationType.equals("EXIT")) {
                break;
            }

            OperationTypes operationType;
            try {
                operationType = OperationTypes.valueOf(inputOperationType);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid operation type: " + inputOperationType);
                continue;
            }

            try {
                Operation operation = operationFactory.getOperation(operationType);
                operation.execute(scanner);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void run() {
        mainMenu();
    }
}
