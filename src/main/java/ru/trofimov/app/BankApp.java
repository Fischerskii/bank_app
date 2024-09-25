package ru.trofimov.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.trofimov.app.service.OperationsConsoleListener;

public class BankApp {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext("ru.trofimov.app");

        OperationsConsoleListener listener = context.getBean(OperationsConsoleListener.class);

        Thread thread = new Thread(listener);
        thread.start();

    }
}
