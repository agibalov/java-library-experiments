package com.loki2302.expectations;

import static org.junit.Assert.assertEquals;

import com.loki2302.dom.DOMBinaryExpression;
import com.loki2302.dom.DOMBinaryExpressionType;

public class BinaryExpressionHasSpecificTypeExpectation implements BinaryExpressionExpectation {
	private final DOMBinaryExpressionType expressionType;
	
	public BinaryExpressionHasSpecificTypeExpectation(DOMBinaryExpressionType expressionType) {
		this.expressionType = expressionType;
	}

	@Override
	public void check(DOMBinaryExpression domBinaryExpression) {
		assertEquals(expressionType, domBinaryExpression.getExpressionType());			
	}		
}