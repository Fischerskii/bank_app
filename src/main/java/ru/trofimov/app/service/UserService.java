package ru.trofimov.app.service;

import ru.trofimov.app.entity.User;

import java.util.List;

public interface UserService {
    User createUser(String login);
    User getUserById(int userId);
    List<User> getAllUsers();
    void addAccountForUser(int userId, int accountId);
    void deleteAccountId(int accountId);
}
