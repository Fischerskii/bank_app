package ru.trofimov.app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountProperties {

    private final BigDecimal defaultMoneyAmount;

    private final double transferCommission;

    public AccountProperties(@Value("${account.transfer-commission}") double transferCommission,
                             @Value("${account.default-amount}") BigDecimal defaultMoneyAmount) {
        this.transferCommission = transferCommission;
        this.defaultMoneyAmount = defaultMoneyAmount;
    }

    public BigDecimal getDefaultMoneyAmount() {
        return defaultMoneyAmount;
    }

    public double getTransferCommission() {
        return transferCommission;
    }
}
