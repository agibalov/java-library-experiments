package com.loki2302.expectations.element.expression.literal;

import static org.junit.Assert.assertEquals;

import com.loki2302.dom.DOMLiteralExpression;
import com.loki2302.dom.DOMLiteralType;

public class LiteralExpressionHasSpecificTypeExpectation implements LiteralExpressionExpectation {
	private final DOMLiteralType literalType;
	
	public LiteralExpressionHasSpecificTypeExpectation(DOMLiteralType literalType) {
		this.literalType = literalType;
	}

	@Override
	public void check(DOMLiteralExpression domLiteralExpression) {
		assertEquals(literalType, domLiteralExpression.getLiteralType());			
	}
}