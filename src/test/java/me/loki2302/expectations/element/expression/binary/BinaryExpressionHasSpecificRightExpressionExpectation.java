package me.loki2302.expectations.element.expression.binary;

import me.loki2302.dom.DOMBinaryExpression;
import me.loki2302.dom.DOMExpression;
import me.loki2302.expectations.element.expression.ExpressionExpectation;

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