package com.loki2302.expectations.element.expression;

import static org.junit.Assert.*;

import com.loki2302.dom.DOMBinaryExpression;
import com.loki2302.dom.DOMExpression;
import com.loki2302.expectations.element.expression.binary.BinaryExpressionExpectation;

public class ExpressionIsBinaryExpressionExpectation implements ExpressionExpectation {
	private final BinaryExpressionExpectation[] expectations;
	
	public ExpressionIsBinaryExpressionExpectation() {
		this(new BinaryExpressionExpectation[]{});
	}
	
	public ExpressionIsBinaryExpressionExpectation(BinaryExpressionExpectation[] expectations) {
		this.expectations = expectations;
	}

	@Override
	public void check(DOMExpression domExpression) {
		assertTrue(domExpression instanceof DOMBinaryExpression);
		DOMBinaryExpression domBinaryExpression = (DOMBinaryExpression)domExpression;
		for(BinaryExpressionExpectation expectation : expectations) {
			expectation.check(domBinaryExpression);
		}			
	}
}