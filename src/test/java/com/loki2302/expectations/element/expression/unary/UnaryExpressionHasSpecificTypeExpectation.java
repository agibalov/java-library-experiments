package com.loki2302.expectations.element.expression.unary;

import static org.junit.Assert.assertEquals;

import com.loki2302.dom.DOMUnaryExpression;
import com.loki2302.dom.DOMUnaryExpressionType;

public class UnaryExpressionHasSpecificTypeExpectation implements UnaryExpressionExpectation {
    private final DOMUnaryExpressionType expressionType;
    
    public UnaryExpressionHasSpecificTypeExpectation(DOMUnaryExpressionType expressionType) {
        this.expressionType = expressionType;
    }

    @Override
    public void check(DOMUnaryExpression domUnaryExpression) {
        assertEquals(expressionType, domUnaryExpression.getExpressionType());             
    }	    
}