package com.loki2302.expectations.element.expression.functioncall;

import static org.junit.Assert.*;

import java.util.List;

import com.loki2302.dom.DOMExpression;
import com.loki2302.dom.DOMFunctionCallExpression;
import com.loki2302.expectations.element.expression.ExpressionExpectation;

public class FunctionCallHasParameterN implements FunctionCallExpressionExpectation {
    private final int parameterIndex;
    private final ExpressionExpectation[] expressionExpectations;
    
    public FunctionCallHasParameterN(
            int parameterIndex, 
            ExpressionExpectation[] expressionExpectations) {
        this.parameterIndex = parameterIndex;
        this.expressionExpectations = expressionExpectations;
    }

    @Override
    public void check(DOMFunctionCallExpression domFunctionCallExpression) {
        List<DOMExpression> parameters = domFunctionCallExpression.getParameters(); 
        assertTrue(parameters.size() > parameterIndex - 1);
        DOMExpression parameterExpression = parameters.get(parameterIndex);
        for(ExpressionExpectation expressionExpectation : expressionExpectations) {
            expressionExpectation.check(parameterExpression);
        }
    }
}