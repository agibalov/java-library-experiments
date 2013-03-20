package com.loki2302.dom;

public class DOMExplicitCastExpression implements DOMExpression {
    private final DOMTypeReference typeReference;
    private final DOMExpression innerExpression;
    
    public DOMExplicitCastExpression(
            DOMTypeReference typeReference, 
            DOMExpression innerExpression) {
        this.typeReference = typeReference;
        this.innerExpression = innerExpression;
    }
    
    public DOMTypeReference getTypeReference() {
        return typeReference;
    }
    
    public DOMExpression getInnerExpression() {
        return innerExpression;
    }
}