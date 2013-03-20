package com.loki2302.dom;

public class DOMReturnStatement implements DOMStatement {
    private final DOMExpression expression;
    
    public DOMReturnStatement(DOMExpression expression) {
        this.expression = expression;
    }
    
    public DOMExpression getExpression() {
        return expression;
    }
}