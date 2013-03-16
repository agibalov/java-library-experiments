package com.loki2302.expectations;

import static org.junit.Assert.assertEquals;

import com.loki2302.dom.DOMElement;
import com.loki2302.dom.DOMLiteralExpression;


public class IsDOMLiteralExpressionExpectation implements DOMElementExpectation {
	private final DOMLiteralExpressionExpectation[] expectations;
	
	public IsDOMLiteralExpressionExpectation() {
		this(new DOMLiteralExpressionExpectation[]{});
	}
	
	public IsDOMLiteralExpressionExpectation(DOMLiteralExpressionExpectation[] expectations) {
		this.expectations = expectations;
	}
	
	public void check(DOMElement domElement) {
		assertEquals(DOMLiteralExpression.class, domElement.getClass());
		DOMLiteralExpression domLiteralExpression = (DOMLiteralExpression)domElement;
		for(DOMLiteralExpressionExpectation expectation : expectations) {
			expectation.check(domLiteralExpression);
		}
	}
}