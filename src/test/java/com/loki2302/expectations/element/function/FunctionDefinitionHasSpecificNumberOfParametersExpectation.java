package com.loki2302.expectations.element.function;

import static org.junit.Assert.assertEquals;

import com.loki2302.dom.DOMFunctionDefinition;

public class FunctionDefinitionHasSpecificNumberOfParametersExpectation implements FunctionDefinitionExpectation {
    private final int parameterCount;
    
    public FunctionDefinitionHasSpecificNumberOfParametersExpectation(int parameterCount) {
        this.parameterCount = parameterCount;
    }
    
    @Override
    public void check(DOMFunctionDefinition domFunctionDefinition) {
        assertEquals(parameterCount, domFunctionDefinition.getParameterDefinitions().size());
    }
}