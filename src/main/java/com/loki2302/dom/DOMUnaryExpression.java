package com.loki2302.dom;

public class DOMUnaryExpression implements DOMExpression {
    private DOMUnaryExpressionType expressionType;
    private DOMExpression innerExpression;
    
    public DOMUnaryExpression(
            DOMUnaryExpressionType expressionType, 
            DOMExpression innerExpression) {
        
        this.expressionType = expressionType;
        this.innerExpression = innerExpression;
    }
    
    public DOMUnaryExpressionType getExpressionType() {
        return expressionType;
    }
    
    public DOMExpression innerExpression() {
        return innerExpression;
    }
}