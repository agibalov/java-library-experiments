package com.loki2302.expectations.element.statement.forstatement;

import static org.junit.Assert.assertNull;

import com.loki2302.dom.DOMForStatement;
import com.loki2302.dom.DOMStatement;

public class ForStatementHasNoStepStatementExpectation implements ForStatementExpectation {        
    @Override
    public void check(DOMForStatement domForStatement) {
        DOMStatement stepStatement = domForStatement.getStepStatement();
        assertNull(stepStatement);
    }        
}