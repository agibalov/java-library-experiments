package com.loki2302.expectations.element.function;

import static org.junit.Assert.assertEquals;

import com.loki2302.dom.DOMFunctionDefinition;

public class FunctionDefinitionHasSpeicifcFunctioNameExpectation implements FunctionDefinitionExpectation {
    private final String functionName;
    
    public FunctionDefinitionHasSpeicifcFunctioNameExpectation(String functionName) {
        this.functionName = functionName;
    }
    
    @Override
    public void check(DOMFunctionDefinition domFunctionDefinition) {
        assertEquals(functionName, domFunctionDefinition.getFunctionName());
    }	    
}