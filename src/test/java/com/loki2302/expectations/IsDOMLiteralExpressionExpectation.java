package com.loki2302.expectations;

import static org.junit.Assert.*;

import com.loki2302.DOMExpressionExpectation;
import com.loki2302.dom.DOMExpression;
import com.loki2302.dom.DOMLiteralExpression;

public class IsDOMLiteralExpressionExpectation implements DOMExpressionExpectation {
	private final DOMLiteralExpressionExpectation[] expectations;
	
	public IsDOMLiteralExpressionExpectation() {
		this(new DOMLiteralExpressionExpectation[]{});
	}
	
	public IsDOMLiteralExpressionExpectation(DOMLiteralExpressionExpectation[] expectations) {
		this.expectations = expectations;
	}
	
	public void check(DOMExpression domExpression) {
		assertTrue(domExpression instanceof DOMLiteralExpression);
		DOMLiteralExpression domLiteralExpression = (DOMLiteralExpression)domExpression;
		for(DOMLiteralExpressionExpectation expectation : expectations) {
			expectation.check(domLiteralExpression);
		}
	}
}