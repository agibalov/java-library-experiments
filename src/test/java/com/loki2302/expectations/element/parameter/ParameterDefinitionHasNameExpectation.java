package com.loki2302.expectations.element.parameter;

import static org.junit.Assert.assertEquals;

import com.loki2302.dom.DOMParameterDefinition;

public class ParameterDefinitionHasNameExpectation implements ParameterDefinitionExpectation {
    private final String parameterName;
    
    public ParameterDefinitionHasNameExpectation(String parameterName) {
        this.parameterName = parameterName;
    }
    
    @Override
    public void check(DOMParameterDefinition domFunctionDefinition) {
        assertEquals(parameterName, domFunctionDefinition.getName());
    }
}