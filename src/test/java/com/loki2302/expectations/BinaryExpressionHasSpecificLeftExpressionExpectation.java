package com.loki2302.expectations;

import com.loki2302.dom.DOMBinaryExpression;
import com.loki2302.dom.DOMExpression;

public class BinaryExpressionHasSpecificLeftExpressionExpectation implements BinaryExpressionExpectation {
	private final ExpressionExpectation[] expectations;
	
	public BinaryExpressionHasSpecificLeftExpressionExpectation(ExpressionExpectation[] expectations) {
		this.expectations = expectations;
	}
	
	@Override
	public void check(DOMBinaryExpression domBinaryExpression) {
		DOMExpression leftExpression = domBinaryExpression.getLeftExpression();
		for(ExpressionExpectation expectation : expectations) {
			expectation.check(leftExpression);
		}
	}
}