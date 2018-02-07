package me.loki2302.expectations.element.statement.returnstatement;

import static org.junit.Assert.assertNull;
import me.loki2302.dom.DOMExpression;
import me.loki2302.dom.DOMReturnStatement;

public class ReturnStatementHasNoExpressionExpectation implements ReturnStatementExpectation {
    @Override
    public void check(DOMReturnStatement domReturnStatement) {
        DOMExpression domExpression = domReturnStatement.getExpression();
        assertNull(domExpression);
    }
}