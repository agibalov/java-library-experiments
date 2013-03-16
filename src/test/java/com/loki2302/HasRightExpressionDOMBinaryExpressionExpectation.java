package com.loki2302;

import com.loki2302.dom.DOMBinaryExpression;
import com.loki2302.dom.DOMExpression;
import com.loki2302.expectations.DOMBinaryExpressionExpectation;

public class HasRightExpressionDOMBinaryExpressionExpectation implements DOMBinaryExpressionExpectation {
	private final DOMExpressionExpectation[] expectations;
	
	public HasRightExpressionDOMBinaryExpressionExpectation(DOMExpressionExpectation[] expectations) {
		this.expectations = expectations;
	}
	
	@Override
	public void check(DOMBinaryExpression domBinaryExpression) {
		DOMExpression rightExpression = domBinaryExpression.getRightExpression();
		for(DOMExpressionExpectation expectation : expectations) {
			expectation.check(rightExpression);
		}
	}		
}