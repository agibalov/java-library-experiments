package com.loki2302;

import com.loki2302.dom.DOMBinaryExpressionType;
import com.loki2302.dom.DOMLiteralType;
import com.loki2302.expectations.DOMBinaryExpressionExpectation;
import com.loki2302.expectations.DOMElementExpectation;
import com.loki2302.expectations.DOMLiteralExpressionExpectation;
import com.loki2302.expectations.HasDOMBinaryExpressionTypeExpectation;
import com.loki2302.expectations.HasDOMLiteralTypeExpectation;
import com.loki2302.expectations.HasStringValueExpectation;
import com.loki2302.expectations.IsDOMBinaryExpressionExpectation;
import com.loki2302.expectations.IsDOMExpressionExpectation;
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
	
	protected static DOMElementExpectation isExpression(DOMExpressionExpectation... expectations) {
		return new IsDOMExpressionExpectation(expectations);
	}
	
	protected static DOMExpressionExpectation isLiteral(DOMLiteralExpressionExpectation... expectations) {
		return new IsDOMLiteralExpressionExpectation(expectations);
	}
	
	protected static DOMLiteralExpressionExpectation ofType(DOMLiteralType literalType) {
		return new HasDOMLiteralTypeExpectation(literalType);
	}
	
	protected static DOMLiteralExpressionExpectation havingValueOf(String stringValue) {
		return new HasStringValueExpectation(stringValue);
	}
	
	protected static DOMExpressionExpectation isBinary(DOMBinaryExpressionExpectation... expectations) {
		return new IsDOMBinaryExpressionExpectation(expectations);
	}
	
	protected static DOMBinaryExpressionExpectation ofType(DOMBinaryExpressionType expressionType) {
		return new HasDOMBinaryExpressionTypeExpectation(expressionType);
	}
	
	protected static ExpressionParser parseInt() {
		return new IntExpressionParser();
	}
	
	protected static ExpressionParser parseDouble() {
		return new DoubleExpressionParser();
	}
	
	protected static ExpressionParser parseBool() {
		return new BoolExpressionParser();
	}
	
	protected static ExpressionParser parseLiteral() {
		return new LiteralExpressionParser();
	}
	
	protected static ExpressionParser parseMulDiv() {
		return new MulDivExpressionParser();
	}
	
	protected static ExpressionParser parseAddSub() {
		return new AddSubExpressionParser();
	}
	
	protected static DOMBinaryExpressionExpectation havingLeftExpression(DOMExpressionExpectation... leftExpressionExpectations) {
		return new HasLeftExpressionDOMBinaryExpressionExpectation(leftExpressionExpectations);
	}
	
	protected static DOMBinaryExpressionExpectation havingRightExpression(DOMExpressionExpectation... rightExpressionExpectations) {
		return new HasRightExpressionDOMBinaryExpressionExpectation(rightExpressionExpectations);
	}
}