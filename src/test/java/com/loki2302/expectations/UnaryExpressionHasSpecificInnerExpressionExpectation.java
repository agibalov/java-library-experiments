package com.loki2302.expectations;

import com.loki2302.dom.DOMExpression;
import com.loki2302.dom.DOMUnaryExpression;

public class UnaryExpressionHasSpecificInnerExpressionExpectation implements UnaryExpressionExpectation {
    private final ExpressionExpectation[] expectations;
    
    public UnaryExpressionHasSpecificInnerExpressionExpectation(ExpressionExpectation[] expectations) {
        this.expectations = expectations;
    }
    
    @Override
    public void check(DOMUnaryExpression domUnaryExpression) {
        DOMExpression innerExpression = domUnaryExpression.getInnerExpression();
        for(ExpressionExpectation expectation : expectations) {
            expectation.check(innerExpression);
        }
    }	    
}