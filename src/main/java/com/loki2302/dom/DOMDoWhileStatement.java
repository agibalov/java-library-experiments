package com.loki2302.dom;

public class DOMDoWhileStatement implements DOMStatement {
    private final DOMExpression conditionExpression;
    private final DOMStatement bodyStatement;
    
    public DOMDoWhileStatement(
            DOMExpression conditionExpression, 
            DOMStatement bodyStatement) {
        this.conditionExpression = conditionExpression;
        this.bodyStatement = bodyStatement;
    }
    
    public DOMExpression getConditionExpression() {
        return conditionExpression;
    }
    
    public DOMStatement getBodyStatement() {
        return bodyStatement;
    }
}