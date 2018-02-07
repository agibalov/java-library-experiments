package me.loki2302.expectations.element.statement.ifstatement;

import static org.junit.Assert.assertNotNull;
import me.loki2302.dom.DOMExpression;
import me.loki2302.dom.DOMIfStatement;
import me.loki2302.expectations.element.expression.ExpressionExpectation;

public class IfStatementHasConditionExpressionExpectation implements IfStatementExpectation {
    private final ExpressionExpectation[] expectations;
    
    public IfStatementHasConditionExpressionExpectation(ExpressionExpectation[] expectations) {
        this.expectations = expectations;
    }

    @Override
    public void check(DOMIfStatement domIfStatement) {
        DOMExpression conditionExpression = domIfStatement.getConditionExpression(); 
        assertNotNull(conditionExpression);            
        for(ExpressionExpectation expectation : expectations) {
            expectation.check(conditionExpression);
        }
    }
}