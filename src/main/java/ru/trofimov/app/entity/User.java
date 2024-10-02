package ru.trofimov.app.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_login",
            nullable = false)
    private String login;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Account> accounts;

    public User() {
    }

    public User(String login, List<Account> accounts) {
        this.login = login;
        this.accounts = accounts;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", accounts=" + accounts +
                '}';
    }
}
