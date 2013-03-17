package com.loki2302.expectations.element.expression.variable;

import static org.junit.Assert.assertEquals;

import com.loki2302.dom.DOMVariableReferenceExpression;

public class VariableHasSpecificNameExpectation implements VariableReferenceExpectation {
    private final String variableName;
    
    public VariableHasSpecificNameExpectation(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public void check(DOMVariableReferenceExpression domVariableReferenceExpression) {
        assertEquals(variableName, domVariableReferenceExpression.getVariableName());
    }
}