package com.loki2302.expectations.element.expression.functioncall;


import com.loki2302.dom.DOMFunctionCallExpression;

import static org.junit.Assert.*;

public class FunctionCallIsForSpecificFunctionNameExpectation implements FunctionCallExpressionExpectation {
    private final String functionName;
    
    public FunctionCallIsForSpecificFunctionNameExpectation(String functionName) {
        this.functionName = functionName;
    }

    @Override
    public void check(DOMFunctionCallExpression domFunctionCallExpression) {
        assertEquals(functionName, domFunctionCallExpression.getFunctionName());            
    }
}