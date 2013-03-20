package com.loki2302.expectations.element.statement.variabledefinition;

import static org.junit.Assert.assertNotNull;

import com.loki2302.dom.DOMTypeReference;
import com.loki2302.dom.DOMVariableDefinitionStatement;
import com.loki2302.expectations.element.typereference.TypeReferenceExpectation;

public class VariableDefinitionHasTypeReferenceExpectation implements VariableDefinitionStatementExpectation {
    private final TypeReferenceExpectation[] expectations;
    
    public VariableDefinitionHasTypeReferenceExpectation(TypeReferenceExpectation[] expectations) {
        this.expectations = expectations;
    }
    
    @Override
    public void check(DOMVariableDefinitionStatement domVariableDefinitionStatement) {
        DOMTypeReference typeReference = domVariableDefinitionStatement.getTypeReference();
        assertNotNull(typeReference);
        for(TypeReferenceExpectation expectation : expectations) {
            expectation.check(typeReference);
        }
    }	    
}