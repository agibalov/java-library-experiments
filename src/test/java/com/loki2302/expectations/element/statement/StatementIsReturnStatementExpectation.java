package com.loki2302.expectations.element.statement;

import static org.junit.Assert.assertTrue;

import com.loki2302.dom.DOMReturnStatement;
import com.loki2302.dom.DOMStatement;
import com.loki2302.expectations.element.statement.returnstatement.ReturnStatementExpectation;

public class StatementIsReturnStatementExpectation implements StatementExpectation {
    private final ReturnStatementExpectation[] expectations;
    
    public StatementIsReturnStatementExpectation(ReturnStatementExpectation[] expectations) {
        this.expectations = expectations;
    }
    
    @Override
    public void check(DOMStatement domStatement) {
        assertTrue(domStatement instanceof DOMReturnStatement);
        DOMReturnStatement domReturnStatement = (DOMReturnStatement)domStatement;
        for(ReturnStatementExpectation expectation : expectations) {
            expectation.check(domReturnStatement);
        }
    }
}