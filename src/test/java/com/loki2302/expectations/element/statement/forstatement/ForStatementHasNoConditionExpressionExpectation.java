package com.loki2302.expectations.element.statement.forstatement;

import static org.junit.Assert.assertNull;

import com.loki2302.dom.DOMExpression;
import com.loki2302.dom.DOMForStatement;

public class ForStatementHasNoConditionExpressionExpectation implements ForStatementExpectation {
    @Override
    public void check(DOMForStatement domForStatement) {
        DOMExpression conditionExpression = domForStatement.getConditionExpression();
        assertNull(conditionExpression);
    }        
}