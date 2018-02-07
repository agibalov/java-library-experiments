package me.loki2302.expectations.element.expression;

import static org.junit.Assert.assertTrue;
import me.loki2302.dom.DOMExplicitCastExpression;
import me.loki2302.dom.DOMExpression;
import me.loki2302.expectations.element.expression.cast.ExplicitCastExpressionExpectation;

public class ExpressionIsExplicitCastExpressionExpectation implements ExpressionExpectation {
    private final ExplicitCastExpressionExpectation[] expectations;
    
    public ExpressionIsExplicitCastExpressionExpectation(ExplicitCastExpressionExpectation[] expectations) {
        this.expectations = expectations;
    }
    
    @Override
    public void check(DOMExpression domExpression) {
        assertTrue(domExpression instanceof DOMExplicitCastExpression);
        DOMExplicitCastExpression domExplicitCastExpression = (DOMExplicitCastExpression)domExpression;
        for(ExplicitCastExpressionExpectation expectation : expectations) {
            expectation.check(domExplicitCastExpression);
        }            
    }	    
}