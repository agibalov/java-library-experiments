package com.loki2302.expectations;

import com.loki2302.dom.DOMBinaryExpression;
import com.loki2302.dom.DOMExpression;

public class BinaryExpressionHasSpecificRightExpressionExpectation implements BinaryExpressionExpectation {
	private final ExpressionExpectation[] expectations;
	
	public BinaryExpressionHasSpecificRightExpressionExpectation(ExpressionExpectation[] expectations) {
		this.expectations = expectations;
	}
	
	@Override
	public void check(DOMBinaryExpression domBinaryExpression) {
		DOMExpression rightExpression = domBinaryExpression.getRightExpression();
		for(ExpressionExpectation expectation : expectations) {
			expectation.check(rightExpression);
		}
	}		
}