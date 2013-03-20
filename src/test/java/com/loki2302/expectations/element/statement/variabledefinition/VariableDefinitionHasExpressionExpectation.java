package com.loki2302.expectations.element.statement.variabledefinition;

import static org.junit.Assert.assertNotNull;

import com.loki2302.dom.DOMExpression;
import com.loki2302.dom.DOMVariableDefinitionStatement;
import com.loki2302.expectations.element.expression.ExpressionExpectation;

public class VariableDefinitionHasExpressionExpectation implements VariableDefinitionStatementExpectation {
    private final ExpressionExpectation[] expectations;
    
    public VariableDefinitionHasExpressionExpectation(ExpressionExpectation[] expectations) {
        this.expectations = expectations;
    }
    
    @Override
    public void check(DOMVariableDefinitionStatement domVariableDefinitionStatement) {
        DOMExpression expression = domVariableDefinitionStatement.getExpression();
        assertNotNull(expression);
        for(ExpressionExpectation expectation : expectations) {
            expectation.check(expression);
        }
    }       
}