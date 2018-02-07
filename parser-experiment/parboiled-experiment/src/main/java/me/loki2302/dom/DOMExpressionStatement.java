package me.loki2302.dom;

public class DOMExpressionStatement implements DOMStatement {
    private final DOMExpression expression;
    
    public DOMExpressionStatement(DOMExpression expression) {
        this.expression = expression;
    }
    
    public DOMExpression getExpression() {
        return expression;
    }
}