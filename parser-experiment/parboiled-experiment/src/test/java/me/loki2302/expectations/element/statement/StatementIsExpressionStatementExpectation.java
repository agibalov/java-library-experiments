package me.loki2302.expectations.element.statement;

import static org.junit.Assert.assertTrue;
import me.loki2302.dom.DOMExpressionStatement;
import me.loki2302.dom.DOMStatement;
import me.loki2302.expectations.element.statement.expression.ExpressionStatementExpectation;

public class StatementIsExpressionStatementExpectation implements StatementExpectation {
    private final ExpressionStatementExpectation[] expectations;
    
    public StatementIsExpressionStatementExpectation() {
        this(new ExpressionStatementExpectation[]{});
    }
    
    public StatementIsExpressionStatementExpectation(ExpressionStatementExpectation[] expectations) {
        this.expectations = expectations;
    }

    @Override
    public void check(DOMStatement domStatement) {
        assertTrue(domStatement instanceof DOMExpressionStatement);
        DOMExpressionStatement domExpressionStatement = (DOMExpressionStatement)domStatement;
        for(ExpressionStatementExpectation expectation : expectations) {
            expectation.check(domExpressionStatement);
        }
    }
}