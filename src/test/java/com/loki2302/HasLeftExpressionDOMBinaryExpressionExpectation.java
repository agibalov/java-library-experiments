package com.loki2302;

import com.loki2302.dom.DOMBinaryExpression;
import com.loki2302.dom.DOMExpression;
import com.loki2302.expectations.DOMBinaryExpressionExpectation;

public class HasLeftExpressionDOMBinaryExpressionExpectation implements DOMBinaryExpressionExpectation {
	private final DOMExpressionExpectation[] expectations;
	
	public HasLeftExpressionDOMBinaryExpressionExpectation(DOMExpressionExpectation[] expectations) {
		this.expectations = expectations;
	}
	
	@Override
	public void check(DOMBinaryExpression domBinaryExpression) {
		DOMExpression leftExpression = domBinaryExpression.getLeftExpression();
		for(DOMExpressionExpectation expectation : expectations) {
			expectation.check(leftExpression);
		}
	}		
}