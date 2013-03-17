package com.loki2302.expectations;

import static org.junit.Assert.assertTrue;

import com.loki2302.dom.DOMExpression;
import com.loki2302.dom.DOMUnaryExpression;

public class ExpressionIsUnaryExpressionExpectation implements ExpressionExpectation {
    private final UnaryExpressionExpectation[] expectations;
    
    public ExpressionIsUnaryExpressionExpectation() {
        this(new UnaryExpressionExpectation[]{});
    }
    
    public ExpressionIsUnaryExpressionExpectation(UnaryExpressionExpectation[] expectations) {
        this.expectations = expectations;
    }

    @Override
    public void check(DOMExpression domExpression) {
        assertTrue(domExpression instanceof DOMUnaryExpression);
        DOMUnaryExpression domBinaryExpression = (DOMUnaryExpression)domExpression;
        for(UnaryExpressionExpectation expectation : expectations) {
            expectation.check(domBinaryExpression);
        }            
    }
}