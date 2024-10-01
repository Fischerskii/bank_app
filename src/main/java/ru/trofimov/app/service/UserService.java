package ru.trofimov.app.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final AccountProperties accountProperties;

    @Autowired
    public UserService(TransactionHelper transactionHelper, SessionFactory sessionFactory, AccountProperties accountProperties) {
        this.transactionHelper = transactionHelper;
        this.sessionFactory = sessionFactory;
        this.accountProperties = accountProperties;
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
            session.flush();

            Account account = new Account(newUser, accountProperties.getDefaultMoneyAmount());
            newUser.getAccounts().add(account);
            session.persist(account);

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

    public void deleteAccountId(Long accountId) {
        transactionHelper.executeInTransaction(session -> {
            session.remove(session.get(Account.class, accountId));
        });
    }
}
