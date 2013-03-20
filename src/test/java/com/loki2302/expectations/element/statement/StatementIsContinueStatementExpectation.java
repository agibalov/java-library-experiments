package com.loki2302.expectations.element.statement;

import static org.junit.Assert.assertTrue;

import com.loki2302.dom.DOMContinueStatement;
import com.loki2302.dom.DOMStatement;

public class StatementIsContinueStatementExpectation implements StatementExpectation {
    @Override
    public void check(DOMStatement domStatement) {
        assertTrue(domStatement instanceof DOMContinueStatement);
    }
}