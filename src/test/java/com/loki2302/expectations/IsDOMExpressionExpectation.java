package com.loki2302.expectations;

import static org.junit.Assert.*;

import com.loki2302.DOMExpressionExpectation;
import com.loki2302.dom.DOMElement;
import com.loki2302.dom.DOMExpression;

public class IsDOMExpressionExpectation implements DOMElementExpectation {
	private final DOMExpressionExpectation[] expectations;
	
	public IsDOMExpressionExpectation() {
		this(new DOMExpressionExpectation[]{});
	}
	
	public IsDOMExpressionExpectation(DOMExpressionExpectation[] expectations) {
		this.expectations = expectations;
	}
	
	@Override
	public void check(DOMElement domElement) {
		assertTrue(domElement instanceof DOMExpression);
		DOMExpression domExpression = (DOMExpression)domElement;
		for(DOMExpressionExpectation expectation : expectations) {
			expectation.check(domExpression);
		}
	}		
}