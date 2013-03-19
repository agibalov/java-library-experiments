package com.loki2302.expectations.element.statement.expression;

import com.loki2302.dom.DOMExpression;
import com.loki2302.dom.DOMExpressionStatement;
import com.loki2302.expectations.element.expression.ExpressionExpectation;

public class ExpressionStatementHasSpecificExpressionExpectation implements ExpressionStatementExpectation {
    private final ExpressionExpectation[] expectations;
    
    public ExpressionStatementHasSpecificExpressionExpectation(ExpressionExpectation[] expectations) {
        this.expectations = expectations;
    }

    @Override
    public void check(DOMExpressionStatement domExpressionStatement) {
        DOMExpression domExpression = (DOMExpression)domExpressionStatement.getExpression();
        for(ExpressionExpectation expectation : expectations) {
            expectation.check(domExpression);
        }            
    }        
}