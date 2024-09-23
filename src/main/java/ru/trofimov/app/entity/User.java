package ru.trofimov.app.entity;

import java.util.ArrayList;
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

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public List<Integer> getAccountIds() {
        return accountIds;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setAccounts(List<Integer> accountIds) {
        this.accountIds = accountIds;
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
