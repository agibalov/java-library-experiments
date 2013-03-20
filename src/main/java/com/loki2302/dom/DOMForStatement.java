package com.loki2302.dom;

public class DOMForStatement implements DOMStatement {
    private final DOMStatement initializerStatement;
    private final DOMExpression conditionExpression;
    private final DOMStatement stepStatement;
    private final DOMStatement bodyStatement;
    
    public DOMForStatement(
            DOMStatement initializerStatement, 
            DOMExpression conditionExpression, 
            DOMStatement stepStatement,
            DOMStatement bodyStatement) {
        this.initializerStatement = initializerStatement;
        this.conditionExpression = conditionExpression;
        this.stepStatement = stepStatement;
        this.bodyStatement = bodyStatement;
    }
    
    public DOMStatement getInitializerStatement() {
        return initializerStatement;
    }
    
    public DOMExpression getConditionExpression() {
        return conditionExpression;
    }
    
    public DOMStatement getStepStatement() {
        return stepStatement;
    }
    
    public DOMStatement getBodyStatement() {
        return bodyStatement;
    }
}