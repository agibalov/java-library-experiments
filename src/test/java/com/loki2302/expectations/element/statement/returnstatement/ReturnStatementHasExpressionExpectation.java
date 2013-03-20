package com.loki2302.expectations.element.statement.returnstatement;

import static org.junit.Assert.assertNotNull;

import com.loki2302.dom.DOMExpression;
import com.loki2302.dom.DOMReturnStatement;
import com.loki2302.expectations.element.expression.ExpressionExpectation;

public class ReturnStatementHasExpressionExpectation implements ReturnStatementExpectation {
    private final ExpressionExpectation[] expectations;
    
    public ReturnStatementHasExpressionExpectation(ExpressionExpectation[] expectations) {
        this.expectations = expectations;
    }

    @Override
    public void check(DOMReturnStatement domReturnStatement) {
        DOMExpression domExpression = domReturnStatement.getExpression();
        assertNotNull(domExpression);
        for(ExpressionExpectation expectation : expectations) {
            expectation.check(domExpression);
        }
    }
}