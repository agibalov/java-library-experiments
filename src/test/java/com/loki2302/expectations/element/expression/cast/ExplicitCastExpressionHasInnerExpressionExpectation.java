package com.loki2302.expectations.element.expression.cast;

import static org.junit.Assert.assertNotNull;

import com.loki2302.dom.DOMExplicitCastExpression;
import com.loki2302.dom.DOMExpression;
import com.loki2302.expectations.element.expression.ExpressionExpectation;

public class ExplicitCastExpressionHasInnerExpressionExpectation implements ExplicitCastExpressionExpectation {
    private final ExpressionExpectation[] expectations;
    
    public ExplicitCastExpressionHasInnerExpressionExpectation(ExpressionExpectation[] expectations) {
        this.expectations = expectations;
    }
    
    @Override
    public void check(DOMExplicitCastExpression domExplicitCastExpression) {
        DOMExpression domExpression = domExplicitCastExpression.getInnerExpression();
        assertNotNull(domExpression);
        for(ExpressionExpectation expectation : expectations) {
            expectation.check(domExpression);
        }
    }
}