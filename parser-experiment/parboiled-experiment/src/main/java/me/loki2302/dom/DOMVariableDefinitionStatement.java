package me.loki2302.dom;

public class DOMVariableDefinitionStatement implements DOMStatement {
    private DOMTypeReference typeReference;
    private String variableName;
    private DOMExpression expression;
    
    public DOMVariableDefinitionStatement(
            DOMTypeReference typeReference, 
            String variableName, 
            DOMExpression expression) {
        this.typeReference = typeReference;
        this.variableName = variableName;
        this.expression = expression;
    }
    
    public DOMTypeReference getTypeReference() {
        return typeReference;
    }
    
    public String getVariableName() {
        return variableName;
    }
    
    public DOMExpression getExpression() {
        return expression;
    }
}