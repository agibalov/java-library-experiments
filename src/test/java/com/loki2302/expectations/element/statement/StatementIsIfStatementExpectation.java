package com.loki2302.expectations.element.statement;

import static org.junit.Assert.assertTrue;

import com.loki2302.dom.DOMIfStatement;
import com.loki2302.dom.DOMStatement;
import com.loki2302.expectations.element.statement.ifstatement.IfStatementExpectation;

public class StatementIsIfStatementExpectation implements StatementExpectation {
    private final IfStatementExpectation[] expectations;
    
    public StatementIsIfStatementExpectation(IfStatementExpectation[] expectations) {
        this.expectations = expectations;
    }

    @Override
    public void check(DOMStatement domStatement) {
        assertTrue(domStatement instanceof DOMIfStatement);
        DOMIfStatement domIfStatement = (DOMIfStatement)domStatement;
        for(IfStatementExpectation expectation : expectations) {
            expectation.check(domIfStatement);
        }            
    }        
}