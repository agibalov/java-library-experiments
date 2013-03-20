package com.loki2302.expectations.element.statement.forstatement;

import static org.junit.Assert.assertNotNull;

import com.loki2302.dom.DOMExpression;
import com.loki2302.dom.DOMForStatement;
import com.loki2302.expectations.element.expression.ExpressionExpectation;

public class ForStatementHasConditionExpressionExpectation implements ForStatementExpectation {
    private final ExpressionExpectation[] expectations;
    
    public ForStatementHasConditionExpressionExpectation(ExpressionExpectation[] expectations) {
        this.expectations = expectations;
    }
    
    @Override
    public void check(DOMForStatement domForStatement) {
        DOMExpression conditionExpression = domForStatement.getConditionExpression();
        assertNotNull(conditionExpression);
        for(ExpressionExpectation expectation : expectations) {
            expectation.check(conditionExpression);
        }
    }        
}