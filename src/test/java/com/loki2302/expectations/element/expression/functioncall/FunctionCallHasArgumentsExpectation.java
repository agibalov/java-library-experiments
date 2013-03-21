package com.loki2302.expectations.element.expression.functioncall;

import static org.junit.Assert.assertEquals;

import java.util.List;

import com.loki2302.dom.DOMExpression;
import com.loki2302.dom.DOMFunctionCallExpression;

public class FunctionCallHasArgumentsExpectation implements FunctionCallExpressionExpectation {
    private final int argumentCount;
    
    public FunctionCallHasArgumentsExpectation(int parameterCount) {
        this.argumentCount = parameterCount;
    }
    
    @Override
    public void check(DOMFunctionCallExpression domFunctionCallExpression) {
        List<DOMExpression> arguments = domFunctionCallExpression.getArguments(); 
        assertEquals(argumentCount, arguments.size());
    }
}