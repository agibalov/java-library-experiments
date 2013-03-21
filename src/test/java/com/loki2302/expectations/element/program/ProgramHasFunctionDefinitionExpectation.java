package com.loki2302.expectations.element.program;

import static org.junit.Assert.assertTrue;

import java.util.List;

import com.loki2302.dom.DOMFunctionDefinition;
import com.loki2302.dom.DOMProgram;
import com.loki2302.expectations.element.function.FunctionDefinitionExpectation;

public class ProgramHasFunctionDefinitionExpectation implements ProgramExpectation {
    private final int functionDefinitionIndex;
    private final FunctionDefinitionExpectation[] expectations;
    
    public ProgramHasFunctionDefinitionExpectation(
            int functionDefinitionIndex,
            FunctionDefinitionExpectation[] expectations) {
        this.functionDefinitionIndex = functionDefinitionIndex;
        this.expectations = expectations;
    }

    @Override
    public void check(DOMProgram domProgram) {
        List<DOMFunctionDefinition> functionDefinitions = domProgram.getFunctionDefinitions();
        assertTrue(functionDefinitions.size() > functionDefinitionIndex);
        DOMFunctionDefinition domFunctionDefinition = functionDefinitions.get(functionDefinitionIndex);                    
        for(FunctionDefinitionExpectation expectation : expectations) {
            expectation.check(domFunctionDefinition);
        }            
    }	    
}