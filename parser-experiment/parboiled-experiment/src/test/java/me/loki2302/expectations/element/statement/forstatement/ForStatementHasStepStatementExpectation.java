package me.loki2302.expectations.element.statement.forstatement;

import static org.junit.Assert.assertNotNull;
import me.loki2302.dom.DOMForStatement;
import me.loki2302.dom.DOMStatement;
import me.loki2302.expectations.element.statement.StatementExpectation;

public class ForStatementHasStepStatementExpectation implements ForStatementExpectation {
    private final StatementExpectation[] expectations;
    
    public ForStatementHasStepStatementExpectation(StatementExpectation[] expectations) {
        this.expectations = expectations;
    }
    
    @Override
    public void check(DOMForStatement domForStatement) {
        DOMStatement stepStatement = domForStatement.getStepStatement();
        assertNotNull(stepStatement);
        for(StatementExpectation expectation : expectations) {
            expectation.check(stepStatement);
        }
    }        
}