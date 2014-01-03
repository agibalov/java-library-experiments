package me.loki2302.dom;

public class DOMParameterDefinition implements DOMElement {
    private final String name;
    private final DOMTypeReference type;
    
    public DOMParameterDefinition(String name, DOMTypeReference type) {
        this.name = name;
        this.type = type;
    }
    
    public String getName() {
        return name;
    }
    
    public DOMTypeReference getType() {
        return type;
    }
}