package com.loki2302.expectations;

import static org.junit.Assert.*;

import com.loki2302.dom.DOMExpression;
import com.loki2302.dom.DOMLiteralExpression;

public class ExpressionIsLiteralExpressionExpectation implements ExpressionExpectation {
	private final LiteralExpressionExpectation[] expectations;
	
	public ExpressionIsLiteralExpressionExpectation() {
		this(new LiteralExpressionExpectation[]{});
	}
	
	public ExpressionIsLiteralExpressionExpectation(LiteralExpressionExpectation[] expectations) {
		this.expectations = expectations;
	}
	
	public void check(DOMExpression domExpression) {
		assertTrue(domExpression instanceof DOMLiteralExpression);
		DOMLiteralExpression domLiteralExpression = (DOMLiteralExpression)domExpression;
		for(LiteralExpressionExpectation expectation : expectations) {
			expectation.check(domLiteralExpression);
		}
	}
}