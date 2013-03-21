package com.loki2302.dom;

import java.util.List;

public class DOMProgram implements DOMElement {
    private final List<DOMFunctionDefinition> functionDefinitions;
    
    public DOMProgram(List<DOMFunctionDefinition> functionDefinitions) {
        this.functionDefinitions = functionDefinitions;
    }
    
    public List<DOMFunctionDefinition> getFunctionDefinitions() {
        return functionDefinitions;
    }
}