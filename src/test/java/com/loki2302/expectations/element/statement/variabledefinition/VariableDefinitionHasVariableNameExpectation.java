package com.loki2302.expectations.element.statement.variabledefinition;

import static org.junit.Assert.assertEquals;

import com.loki2302.dom.DOMVariableDefinitionStatement;

public class VariableDefinitionHasVariableNameExpectation implements VariableDefinitionStatementExpectation {
    private final String variableName;
    
    public VariableDefinitionHasVariableNameExpectation(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public void check(DOMVariableDefinitionStatement domVariableDefinitionStatement) {
        assertEquals(variableName, domVariableDefinitionStatement.getVariableName());            
    }
}