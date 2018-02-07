package me.loki2302.expectations.element.statement;

import static org.junit.Assert.assertTrue;
import me.loki2302.dom.DOMDoWhileStatement;
import me.loki2302.dom.DOMStatement;
import me.loki2302.expectations.element.statement.dowhile.DoWhileStatementExpectation;

public class StatementIsDoWhileStatementExpectation implements StatementExpectation {
    private final DoWhileStatementExpectation[] expectations;
    
    public StatementIsDoWhileStatementExpectation(DoWhileStatementExpectation[] expectations) {
        this.expectations = expectations;
    }
    
    @Override
    public void check(DOMStatement domStatement) {
        assertTrue(domStatement instanceof DOMDoWhileStatement);
        DOMDoWhileStatement domDoWhileStatement = (DOMDoWhileStatement)domStatement;
        for(DoWhileStatementExpectation expectation : expectations) {
            expectation.check(domDoWhileStatement);
        }
    }        
}