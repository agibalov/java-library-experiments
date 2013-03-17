package com.loki2302.expectations.element.expression;

import static org.junit.Assert.assertTrue;

import com.loki2302.dom.DOMExpression;
import com.loki2302.dom.DOMVariableReferenceExpression;
import com.loki2302.expectations.element.expression.variable.VariableReferenceExpectation;

public class ExpressionIsVariableReferenceExpectation implements ExpressionExpectation {
    private final VariableReferenceExpectation[] expectations;
    
    public ExpressionIsVariableReferenceExpectation() {
        this(new VariableReferenceExpectation[]{});
    }
    
    public ExpressionIsVariableReferenceExpectation(VariableReferenceExpectation[] expectations) {
        this.expectations = expectations;
    }

    @Override
    public void check(DOMExpression domExpression) {
        assertTrue(domExpression instanceof DOMVariableReferenceExpression);
        DOMVariableReferenceExpression domVariableReferenceExpression = (DOMVariableReferenceExpression)domExpression;
        for(VariableReferenceExpectation expectation : expectations) {
            expectation.check(domVariableReferenceExpression);
        }            
    }
}