package com.loki2302.dom;

import java.util.List;

public class DOMFunctionCallExpression implements DOMExpression {
    private final String functionName;
    private final List<DOMExpression> parameters;
    
    public DOMFunctionCallExpression(
            String functionName,
            List<DOMExpression> parameters) {
        
        this.functionName = functionName;
        this.parameters = parameters;
    }
    
    public String getFunctionName() {
        return functionName;
    }
    
    public List<DOMExpression> getParameters() {
        return parameters;
    }
}