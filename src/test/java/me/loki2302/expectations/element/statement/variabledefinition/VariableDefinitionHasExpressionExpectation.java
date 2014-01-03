package me.loki2302.expectations.element.statement.variabledefinition;

import static org.junit.Assert.assertNotNull;
import me.loki2302.dom.DOMExpression;
import me.loki2302.dom.DOMVariableDefinitionStatement;
import me.loki2302.expectations.element.expression.ExpressionExpectation;

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