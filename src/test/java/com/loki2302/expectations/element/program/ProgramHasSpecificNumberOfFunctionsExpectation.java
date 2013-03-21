package com.loki2302.expectations.element.program;

import static org.junit.Assert.assertEquals;

import java.util.List;

import com.loki2302.dom.DOMFunctionDefinition;
import com.loki2302.dom.DOMProgram;

public class ProgramHasSpecificNumberOfFunctionsExpectation implements ProgramExpectation {
    private final int functionCount;
    
    public ProgramHasSpecificNumberOfFunctionsExpectation(int functionCount) {
        this.functionCount = functionCount;
    }

    @Override
    public void check(DOMProgram domProgram) {
        List<DOMFunctionDefinition> functionDefinitions = domProgram.getFunctionDefinitions();
        assertEquals(functionCount, functionDefinitions.size());
    }	    
}