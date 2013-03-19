package com.loki2302.expectations.element.statement.ifstatement;

import static org.junit.Assert.assertNotNull;

import com.loki2302.dom.DOMIfStatement;
import com.loki2302.dom.DOMStatement;
import com.loki2302.expectations.element.statement.StatementExpectation;

public class IfStatementHasFalseBranchExpectation implements IfStatementExpectation {
    private final StatementExpectation[] expectations;
    
    public IfStatementHasFalseBranchExpectation(StatementExpectation[] expectations) {
        this.expectations = expectations;
    }

    @Override
    public void check(DOMIfStatement domIfStatement) {
        DOMStatement falseBranch = domIfStatement.getFalseBranch(); 
        assertNotNull(falseBranch);            
        for(StatementExpectation expectation : expectations) {
            expectation.check(falseBranch);
        }
    }
}