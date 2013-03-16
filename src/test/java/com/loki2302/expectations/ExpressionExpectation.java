package com.loki2302.expectations;

import com.loki2302.dom.DOMExpression;

public interface ExpressionExpectation {
	void check(DOMExpression domExpression);
}