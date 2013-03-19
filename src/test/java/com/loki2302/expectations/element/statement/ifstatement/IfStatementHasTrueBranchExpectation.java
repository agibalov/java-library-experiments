package com.loki2302.expectations.element.statement.ifstatement;

import static org.junit.Assert.assertNotNull;

import com.loki2302.dom.DOMIfStatement;
import com.loki2302.dom.DOMStatement;
import com.loki2302.expectations.element.statement.StatementExpectation;

public class IfStatementHasTrueBranchExpectation implements IfStatementExpectation {
    private final StatementExpectation[] expectations;
    
    public IfStatementHasTrueBranchExpectation(StatementExpectation[] expectations) {
        this.expectations = expectations;
    }

    @Override
    public void check(DOMIfStatement domIfStatement) {
        DOMStatement trueBranch = domIfStatement.getTrueBranch(); 
        assertNotNull(trueBranch);            
        for(StatementExpectation expectation : expectations) {
            expectation.check(trueBranch);
        }
    }
}