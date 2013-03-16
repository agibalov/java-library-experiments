package com.loki2302.expectations;

import static org.junit.Assert.assertEquals;

import com.loki2302.dom.DOMBinaryExpression;
import com.loki2302.dom.DOMBinaryExpressionType;

public class HasDOMBinaryExpressionTypeExpectation implements DOMBinaryExpressionExpectation {
	private final DOMBinaryExpressionType expressionType;
	
	public HasDOMBinaryExpressionTypeExpectation(DOMBinaryExpressionType expressionType) {
		this.expressionType = expressionType;
	}

	@Override
	public void check(DOMBinaryExpression domBinaryExpression) {
		assertEquals(expressionType, domBinaryExpression.getExpressionType());			
	}		
}