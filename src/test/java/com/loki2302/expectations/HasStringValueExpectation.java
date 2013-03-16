package com.loki2302.expectations;

import static org.junit.Assert.assertEquals;

import com.loki2302.DOMLiteralExpression;


public class HasStringValueExpectation implements DOMLiteralExpressionExpectation {
	private final String stringValue;
	
	public HasStringValueExpectation(String stringValue) {
		this.stringValue = stringValue;
	}

	@Override
	public void check(DOMLiteralExpression domLiteralExpression) {
		assertEquals(stringValue, domLiteralExpression.getStringValue());			
	}		
}