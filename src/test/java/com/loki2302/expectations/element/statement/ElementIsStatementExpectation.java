package com.loki2302.expectations.element.statement;

import static org.junit.Assert.assertTrue;

import com.loki2302.dom.DOMElement;
import com.loki2302.dom.DOMStatement;
import com.loki2302.expectations.element.ElementExpectation;

public class ElementIsStatementExpectation implements ElementExpectation {
    private final StatementExpectation[] expectations;
    
    public ElementIsStatementExpectation() {
        this(new StatementExpectation[]{});
    }
    
    public ElementIsStatementExpectation(StatementExpectation[] expectations) {
        this.expectations = expectations;
    }
    
    @Override
    public void check(DOMElement domElement) {
        assertTrue(domElement instanceof DOMStatement);
        DOMStatement domStatement = (DOMStatement)domElement;
        for(StatementExpectation expectation : expectations) {
            expectation.check(domStatement);
        }
    }
}