package com.loki2302.expectations.element.statement.dowhile;

import static org.junit.Assert.assertNotNull;

import com.loki2302.dom.DOMDoWhileStatement;
import com.loki2302.dom.DOMStatement;
import com.loki2302.expectations.element.statement.StatementExpectation;

public class DoWhileStatementHasBodyStatementExpectation implements DoWhileStatementExpectation {
    private final StatementExpectation[] expectations;
    
    public DoWhileStatementHasBodyStatementExpectation(StatementExpectation[] expectations) {
        this.expectations = expectations;
    }
    
    @Override
    public void check(DOMDoWhileStatement domDoWhileStatement) {
        DOMStatement bodyStatement = domDoWhileStatement.getBodyStatement();
        assertNotNull(bodyStatement);
        for(StatementExpectation expectation : expectations) {
            expectation.check(bodyStatement);
        }
    }        
}