package me.loki2302.expectations.element.function;

import static org.junit.Assert.assertNotNull;
import me.loki2302.dom.DOMFunctionDefinition;
import me.loki2302.dom.DOMTypeReference;
import me.loki2302.expectations.element.typereference.TypeReferenceExpectation;

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