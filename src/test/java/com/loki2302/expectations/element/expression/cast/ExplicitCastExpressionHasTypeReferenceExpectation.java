package com.loki2302.expectations.element.expression.cast;

import static org.junit.Assert.assertNotNull;

import com.loki2302.dom.DOMExplicitCastExpression;
import com.loki2302.dom.DOMTypeReference;
import com.loki2302.expectations.element.typereference.TypeReferenceExpectation;

public class ExplicitCastExpressionHasTypeReferenceExpectation implements ExplicitCastExpressionExpectation {
    private final TypeReferenceExpectation[] expectations;
    
    public ExplicitCastExpressionHasTypeReferenceExpectation(TypeReferenceExpectation[] expectations) {
        this.expectations = expectations;
    }
    
    @Override
    public void check(DOMExplicitCastExpression domExplicitCastExpression) {
        DOMTypeReference domTypeReference = domExplicitCastExpression.getTypeReference();
        assertNotNull(domTypeReference);
        for(TypeReferenceExpectation expectation : expectations) {
            expectation.check(domTypeReference);
        }
    }	    
}