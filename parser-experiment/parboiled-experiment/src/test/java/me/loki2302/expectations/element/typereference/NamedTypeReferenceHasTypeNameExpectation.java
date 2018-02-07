package me.loki2302.expectations.element.typereference;

import static org.junit.Assert.assertEquals;
import me.loki2302.dom.DOMNamedTypeReference;

public class NamedTypeReferenceHasTypeNameExpectation implements NamedTypeReferenceExpectation {
    private final String typeName;
    
    public NamedTypeReferenceHasTypeNameExpectation(String typeName) {
        this.typeName = typeName;
    }
    
    @Override
    public void check(DOMNamedTypeReference domNamedTypeReference) {
        assertEquals(typeName, domNamedTypeReference.getTypeName());            
    }	    
}