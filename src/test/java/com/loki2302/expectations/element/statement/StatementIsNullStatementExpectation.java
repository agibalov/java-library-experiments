package com.loki2302.expectations.element.statement;

import static org.junit.Assert.assertTrue;

import com.loki2302.dom.DOMNullStatement;
import com.loki2302.dom.DOMStatement;

public class StatementIsNullStatementExpectation implements StatementExpectation {
    @Override
    public void check(DOMStatement domStatement) {
        assertTrue(domStatement instanceof DOMNullStatement);
    }    
}