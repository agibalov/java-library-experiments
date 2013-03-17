package com.loki2302.dom;

public class DOMPrintStatement implements DOMStatement {
    private final DOMExpression expression;
    
    public DOMPrintStatement(DOMExpression expression) {
        this.expression = expression;
    }
    
    public DOMExpression getExpression() {
        return expression;
    }
}