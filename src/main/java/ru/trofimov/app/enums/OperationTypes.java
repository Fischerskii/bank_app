package ru.trofimov.app.enums;

import ru.trofimov.app.operations.*;
import ru.trofimov.app.operations.impl.*;

public enum OperationTypes {
    ACCOUNT_CREATE(AccountCreateOperation.class),
    SHOW_ALL_USERS(ShowAllUsersOperation.class),
    ACCOUNT_CLOSE(AccountCloseOperation.class),
    ACCOUNT_WITHDRAW(AccountWithdrawOperation.class),
    ACCOUNT_DEPOSIT(AccountDepositOperation.class),
    ACCOUNT_TRANSFER(AccountTransferOperation.class),
    USER_CREATE(UserCreateOperation.class),
    GET_ACCOUNT(GetAccountOperation.class),
    GET_ALL_USER_ACCOUNTS(GetAllUserAccounts.class);

    private final Class<? extends Operation> operationClass;

    OperationTypes(Class<? extends Operation> operationClass ) {
        this.operationClass = operationClass;
    }

    public Class<? extends Operation> getOperationClass() {
        return operationClass;
    }
}
