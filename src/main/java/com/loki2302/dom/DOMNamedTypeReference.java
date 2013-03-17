package com.loki2302.dom;

public class DOMNamedTypeReference implements DOMTypeReference {
    private final String typeName;
    
    public DOMNamedTypeReference(String typeName) {
        this.typeName = typeName;
    }
    
    public String getTypeName() {
        return typeName;
    }
}