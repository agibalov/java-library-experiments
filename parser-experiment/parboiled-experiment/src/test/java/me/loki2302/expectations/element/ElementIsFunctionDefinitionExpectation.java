package me.loki2302.expectations.element;

import static org.junit.Assert.assertTrue;
import me.loki2302.dom.DOMElement;
import me.loki2302.dom.DOMFunctionDefinition;
import me.loki2302.expectations.element.function.FunctionDefinitionExpectation;

public class ElementIsFunctionDefinitionExpectation implements ElementExpectation {
    private final FunctionDefinitionExpectation[] expectations;
    
    public ElementIsFunctionDefinitionExpectation(FunctionDefinitionExpectation[] expectations) {
        this.expectations = expectations;
    }
    
    @Override
    public void check(DOMElement domElement) {
        assertTrue(domElement instanceof DOMFunctionDefinition);
        DOMFunctionDefinition domFunctionDefinition = (DOMFunctionDefinition)domElement;
        for(FunctionDefinitionExpectation expectation : expectations) {
            expectation.check(domFunctionDefinition);
        }
    }        
}