package me.loki2302.expectations.element.expression.unary;

import me.loki2302.dom.DOMExpression;
import me.loki2302.dom.DOMUnaryExpression;
import me.loki2302.expectations.element.expression.ExpressionExpectation;

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