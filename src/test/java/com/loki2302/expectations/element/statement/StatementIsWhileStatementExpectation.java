package com.loki2302.expectations.element.statement;

import static org.junit.Assert.assertTrue;

import com.loki2302.dom.DOMStatement;
import com.loki2302.dom.DOMWhileStatement;
import com.loki2302.expectations.element.statement.whilestatement.WhileStatementExpectation;

public class StatementIsWhileStatementExpectation implements StatementExpectation {
    private final WhileStatementExpectation[] expectations;
    
    public StatementIsWhileStatementExpectation(WhileStatementExpectation[] expectations) {
        this.expectations = expectations;
    }
    
    @Override
    public void check(DOMStatement domStatement) {
        assertTrue(domStatement instanceof DOMWhileStatement);
        DOMWhileStatement domWhileStatement = (DOMWhileStatement)domStatement;
        for(WhileStatementExpectation expectation : expectations) {
            expectation.check(domWhileStatement);
        }
    }
}