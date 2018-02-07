package me.loki2302.expectations.element.statement;

import static org.junit.Assert.assertTrue;
import me.loki2302.dom.DOMForStatement;
import me.loki2302.dom.DOMStatement;
import me.loki2302.expectations.element.statement.forstatement.ForStatementExpectation;

public class StatementIsForStatementExpectation implements StatementExpectation {
    private final ForStatementExpectation[] expectations;
    
    public StatementIsForStatementExpectation(ForStatementExpectation[] expectations) {
        this.expectations = expectations;
    }
    
    @Override
    public void check(DOMStatement domStatement) {
        assertTrue(domStatement instanceof DOMForStatement);
        DOMForStatement domForStatement = (DOMForStatement)domStatement;
        for(ForStatementExpectation expectation : expectations) {
            expectation.check(domForStatement);
        }
    }        
}