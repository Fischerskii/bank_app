package ru.trofimov.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.trofimov.app.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserService {

    private final Map<Integer, User> users = new HashMap<>();
    private int lastUserId;
    private final AccountService accountService;

    @Autowired
    public UserService(AccountService accountService) {
        this.accountService = accountService;
    }

    public User createUser(String login) {
        for(User user : users.values()) {
            if(user.getLogin().equals(login)) {
                throw new IllegalArgumentException("User with login " + login + " already exists");
            }
        }

        List<Integer> accountIds = new ArrayList<>();
        User user = new User(lastUserId, login, accountIds);
        users.put(lastUserId, user);
        accountService.createAccount(lastUserId);
        lastUserId++;
        return user;
    }

    public User getUserById(int userId) {
        if (users.containsKey(userId)) {
            return users.get(userId);
        } else {
            throw new IllegalArgumentException("User with id " + userId + " not found");
        }
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public void addAccountForUser(int userId, int accountId) {
        users.get(userId).getAccountIds().add(accountId);
    }

    public void deleteAccountId(int accountId) {
        users.values().forEach(u -> u.getAccountIds().remove(accountId));
    }
}
