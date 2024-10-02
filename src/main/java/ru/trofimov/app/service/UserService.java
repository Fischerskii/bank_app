package ru.trofimov.app.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.trofimov.app.entity.Account;
import ru.trofimov.app.entity.User;
import ru.trofimov.app.helper.TransactionHelper;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final TransactionHelper transactionHelper;
    private final SessionFactory sessionFactory;
    private final AccountService accountService;

    @Autowired
    public UserService(TransactionHelper transactionHelper,
                       SessionFactory sessionFactory,
                       @Lazy AccountService accountService) {
        this.transactionHelper = transactionHelper;
        this.sessionFactory = sessionFactory;
        this.accountService = accountService;
    }

    public User createUser(String login) throws IllegalArgumentException {
        return transactionHelper.executeInTransaction(session -> {

            Boolean userExists = session.createQuery("SELECT count(u) > 0 FROM User u WHERE u.login = :login", Boolean.class)
                    .setParameter("login", login)
                    .getSingleResult();

            if (userExists) {
                throw new IllegalArgumentException("User with login " + login + " already exists. " +
                        "Please enter another login.");
            }

            User newUser = new User(login, new ArrayList<>());
            session.persist(newUser);

            Account account = accountService.createAccountForNewUser(newUser, session);
            newUser.getAccounts().add(account);

            session.persist(newUser);
            return newUser;
        });
    }

    public User getUserById(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, userId);
        }
    }

    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session
                    .createQuery("SELECT u FROM User u ", User.class)
                    .list();
        }
    }
}
