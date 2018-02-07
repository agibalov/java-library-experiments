package me.loki2302.expectations.element.typereference;

import static org.junit.Assert.assertTrue;
import me.loki2302.dom.DOMNamedTypeReference;
import me.loki2302.dom.DOMTypeReference;

public class TypeReferenceIsNamedTypeExpectation implements TypeReferenceExpectation {
    private final NamedTypeReferenceExpectation[] expectations;
    
    public TypeReferenceIsNamedTypeExpectation(NamedTypeReferenceExpectation[] expectations) {
        this.expectations = expectations;
    }
    
    @Override
    public void check(DOMTypeReference domTypeReference) {
        assertTrue(domTypeReference instanceof DOMNamedTypeReference);
        DOMNamedTypeReference domNamedTypeReference = (DOMNamedTypeReference)domTypeReference;
        for(NamedTypeReferenceExpectation expectation : expectations) {
            expectation.check(domNamedTypeReference);
        }
    }	    
}