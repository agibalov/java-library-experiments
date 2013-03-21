package com.loki2302.expectations.element.function;

import static org.junit.Assert.assertNotNull;

import com.loki2302.dom.DOMFunctionDefinition;
import com.loki2302.dom.DOMTypeReference;
import com.loki2302.expectations.element.typereference.TypeReferenceExpectation;

public class FunctionDefinitionHasReturnTypeExpectation implements FunctionDefinitionExpectation {
    private final TypeReferenceExpectation[] expectations;
    
    public FunctionDefinitionHasReturnTypeExpectation(TypeReferenceExpectation[] expectations) {
        this.expectations = expectations;
    }
    
    @Override
    public void check(DOMFunctionDefinition domFunctionDefinition) {
        DOMTypeReference domTypeReference = domFunctionDefinition.getReturnType();
        assertNotNull(domTypeReference);
        for(TypeReferenceExpectation expectation : expectations) {
            expectation.check(domTypeReference);
        }
    }	    
}