package me.loki2302.dom;

import java.util.List;

public class DOMFunctionCallExpression implements DOMExpression {
    private final String functionName;
    private final List<DOMExpression> arguments;
    
    public DOMFunctionCallExpression(
            String functionName,
            List<DOMExpression> arguments) {
        
        this.functionName = functionName;
        this.arguments = arguments;
    }
    
    public String getFunctionName() {
        return functionName;
    }
    
    public List<DOMExpression> getArguments() {
        return arguments;
    }
}