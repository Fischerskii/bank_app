package ru.trofimov.app.operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.trofimov.app.enums.OperationTypes;

@Component
public class OperationFactory {

    private final ApplicationContext context;

    @Autowired
    public OperationFactory(ApplicationContext context) {
        this.context = context;
    }

    public Operation getOperation(OperationTypes operationType) {
        return context.getBean(operationType.getOperationClass());
    }
}