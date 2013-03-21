package com.loki2302.expectations.element.function;

import static org.junit.Assert.assertTrue;

import java.util.List;

import com.loki2302.dom.DOMFunctionDefinition;
import com.loki2302.dom.DOMParameterDefinition;
import com.loki2302.expectations.element.parameter.ParameterDefinitionExpectation;

public class FunctionDefinitionHasParameterExpectation implements FunctionDefinitionExpectation {
    private final int parameterIndex;
    private final ParameterDefinitionExpectation[] expectations;
    
    public FunctionDefinitionHasParameterExpectation(
            int parameterIndex,
            ParameterDefinitionExpectation[] expectations) {
        this.parameterIndex = parameterIndex;
        this.expectations = expectations;
    }
    
    @Override
    public void check(DOMFunctionDefinition domFunctionDefinition) {
        List<DOMParameterDefinition> definitions = domFunctionDefinition.getParameterDefinitions();
        assertTrue(definitions.size() > parameterIndex);
        DOMParameterDefinition domParameterDefinition = definitions.get(parameterIndex);
        for(ParameterDefinitionExpectation expectation : expectations) {
            expectation.check(domParameterDefinition);
        }
    }	    
}