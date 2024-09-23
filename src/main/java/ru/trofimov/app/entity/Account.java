package ru.trofimov.app.entity;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

public class Account {
    private final int id;
    private final int userId;
    private BigDecimal moneyAmount;

    @Value("${account.default-amount}")
    private BigDecimal defaultMoneyAmount;

    public Account(int id, int userId) {
        this.id = id;
        this.userId = userId;
        this.moneyAmount = defaultMoneyAmount;
    }

    public Account(int id, int userId, BigDecimal moneyAmount) {
        this.id = id;
        this.userId = userId;
        this.moneyAmount = moneyAmount;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public BigDecimal getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(BigDecimal moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userId=" + userId +
                ", moneyAmount=" + moneyAmount +
                '}';
    }
}
