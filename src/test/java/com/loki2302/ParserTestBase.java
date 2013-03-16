package com.loki2302;

import com.loki2302.expectations.DOMElementExpectation;
import com.loki2302.expectations.DOMLiteralExpressionExpectation;
import com.loki2302.expectations.HasDOMLiteralTypeExpectation;
import com.loki2302.expectations.HasStringValueExpectation;
import com.loki2302.expectations.IsDOMLiteralExpressionExpectation;
import com.loki2302.expectations.ParseResultExpectation;
import com.loki2302.expectations.ParseResultIsBadExpectation;
import com.loki2302.expectations.ParseResultIsOkExpectation;

public abstract class ParserTestBase {
	protected static ParseResultExpectation result(DOMElementExpectation domElementDescriptor) {
		return new ParseResultIsOkExpectation(domElementDescriptor);
	}	
	
	protected static ParseResultExpectation fail() {
		return new ParseResultIsBadExpectation();
	}
	
	protected static DOMElementExpectation isLiteral(DOMLiteralExpressionExpectation... expectations) {
		return new IsDOMLiteralExpressionExpectation(expectations);
	}
	
	protected static DOMLiteralExpressionExpectation havingTypeOf(DOMLiteralType literalType) {
		return new HasDOMLiteralTypeExpectation(literalType);
	}
	
	protected static DOMLiteralExpressionExpectation havingStringValueOf(String stringValue) {
		return new HasStringValueExpectation(stringValue);
	}
}