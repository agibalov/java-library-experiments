package com.loki2302.expectations.element;

import static org.junit.Assert.*;

import com.loki2302.dom.DOMElement;
import com.loki2302.dom.DOMExpression;
import com.loki2302.expectations.element.expression.ExpressionExpectation;

public class ElementIsExpressionExpectation implements ElementExpectation {
	private final ExpressionExpectation[] expectations;
	
	public ElementIsExpressionExpectation() {
		this(new ExpressionExpectation[]{});
	}
	
	public ElementIsExpressionExpectation(ExpressionExpectation[] expectations) {
		this.expectations = expectations;
	}
	
	@Override
	public void check(DOMElement domElement) {
		assertTrue(domElement instanceof DOMExpression);
		DOMExpression domExpression = (DOMExpression)domElement;
		for(ExpressionExpectation expectation : expectations) {
			expectation.check(domExpression);
		}
	}		
}