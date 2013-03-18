package com.loki2302.expectations.element.statement;

import static org.junit.Assert.assertTrue;

import com.loki2302.dom.DOMExpressionStatement;
import com.loki2302.dom.DOMStatement;

public class StatementIsExpressionStatementExpectation implements StatementExpectation {
    private final ExpressionStatementExpectation[] expectations;
    
    public StatementIsExpressionStatementExpectation() {
        this(new ExpressionStatementExpectation[]{});
    }
    
    public StatementIsExpressionStatementExpectation(ExpressionStatementExpectation[] expectations) {
        this.expectations = expectations;
    }

    @Override
    public void check(DOMStatement domStatement) {
        assertTrue(domStatement instanceof DOMExpressionStatement);
        DOMExpressionStatement domExpressionStatement = (DOMExpressionStatement)domStatement;
        for(ExpressionStatementExpectation expectation : expectations) {
            expectation.check(domExpressionStatement);
        }
    }
}