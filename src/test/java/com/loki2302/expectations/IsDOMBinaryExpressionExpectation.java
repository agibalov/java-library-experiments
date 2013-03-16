package com.loki2302.expectations;

import static org.junit.Assert.*;

import com.loki2302.DOMExpressionExpectation;
import com.loki2302.dom.DOMBinaryExpression;
import com.loki2302.dom.DOMExpression;

public class IsDOMBinaryExpressionExpectation implements DOMExpressionExpectation {
	private final DOMBinaryExpressionExpectation[] expectations;
	
	public IsDOMBinaryExpressionExpectation() {
		this(new DOMBinaryExpressionExpectation[]{});
	}
	
	public IsDOMBinaryExpressionExpectation(DOMBinaryExpressionExpectation[] expectations) {
		this.expectations = expectations;
	}

	@Override
	public void check(DOMExpression domExpression) {
		assertTrue(domExpression instanceof DOMBinaryExpression);
		DOMBinaryExpression domBinaryExpression = (DOMBinaryExpression)domExpression;
		for(DOMBinaryExpressionExpectation expectation : expectations) {
			expectation.check(domBinaryExpression);
		}			
	}
}