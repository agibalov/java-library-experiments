package com.loki2302.expectations.element.statement.returnstatement;

import static org.junit.Assert.assertNull;

import com.loki2302.dom.DOMExpression;
import com.loki2302.dom.DOMReturnStatement;

public class ReturnStatementHasNoExpressionExpectation implements ReturnStatementExpectation {
    @Override
    public void check(DOMReturnStatement domReturnStatement) {
        DOMExpression domExpression = domReturnStatement.getExpression();
        assertNull(domExpression);
    }
}