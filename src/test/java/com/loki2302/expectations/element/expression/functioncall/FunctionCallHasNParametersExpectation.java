package com.loki2302.expectations.element.expression.functioncall;

import static org.junit.Assert.assertEquals;

import java.util.List;

import com.loki2302.dom.DOMExpression;
import com.loki2302.dom.DOMFunctionCallExpression;

public class FunctionCallHasNParametersExpectation implements FunctionCallExpressionExpectation {
    private final int parameterCount;
    
    public FunctionCallHasNParametersExpectation(int parameterCount) {
        this.parameterCount = parameterCount;
    }
    
    @Override
    public void check(DOMFunctionCallExpression domFunctionCallExpression) {
        List<DOMExpression> parameters = domFunctionCallExpression.getParameters(); 
        assertEquals(parameterCount, parameters.size());
    }
}