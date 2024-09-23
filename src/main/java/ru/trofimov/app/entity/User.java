package ru.trofimov.app.entity;

import java.util.List;

public class User {
    private final int id;
    private String login;
    private List<Integer> accountIds;

    public User(int id, String login, List<Integer> accountIds) {
        this.id = id;
        this.login = login;
        this.accountIds = accountIds;
    }

    public String getLogin() {
        return login;
    }

    public List<Integer> getAccountIds() {
        return accountIds;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", accountIds=" + accountIds +
                '}';
    }
}
