package me.loki2302.dom;

public class DOMWhileStatement implements DOMStatement {
    private final DOMExpression conditionExpression;
    private final DOMStatement bodyStatement;
    
    public DOMWhileStatement(
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