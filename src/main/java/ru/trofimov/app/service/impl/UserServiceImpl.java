package ru.trofimov.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.trofimov.app.entity.User;
import ru.trofimov.app.exceptions.UserAlreadyContainsException;
import ru.trofimov.app.exceptions.UserNotFoundException;
import ru.trofimov.app.service.AccountService;
import ru.trofimov.app.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserServiceImpl implements UserService {

    private final Map<Integer, User> users = new HashMap<>();
    private int lastUserId;
    private final AccountService accountService;

    @Autowired
    public UserServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }
    @Override
    public User createUser(String login) {
        for(User user : users.values()) {
            if(user.getLogin().equals(login)) {
                throw new UserAlreadyContainsException("User with login " + login + " already exists");
            }
        }

        List<Integer> accountIds = new ArrayList<>();
        User user = new User(lastUserId, login, accountIds);
        users.put(lastUserId, user);
        accountService.createAccount(lastUserId);
        lastUserId++;
        return user;
    }

    @Override
    public User getUserById(int userId) {
        if (users.containsKey(userId)) {
            return users.get(userId);
        } else {
            throw new UserNotFoundException("User with id " + userId + " not found");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void addAccountForUser(int userId, int accountId) {
        users.get(userId).getAccountIds().add(accountId);
    }

    @Override
    public void deleteAccountId(int accountId) {
        users.values().forEach(u -> u.getAccountIds().remove(accountId));
    }
}
