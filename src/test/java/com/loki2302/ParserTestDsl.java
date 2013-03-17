package com.loki2302;

import com.loki2302.dom.DOMBinaryExpressionType;
import com.loki2302.dom.DOMLiteralType;
import com.loki2302.expectations.BinaryExpressionExpectation;
import com.loki2302.expectations.ElementExpectation;
import com.loki2302.expectations.ExpressionExpectation;
import com.loki2302.expectations.LiteralExpressionExpectation;
import com.loki2302.expectations.BinaryExpressionHasSpecificTypeExpectation;
import com.loki2302.expectations.LiteralExpressionHasSpecificTypeExpectation;
import com.loki2302.expectations.BinaryExpressionHasSpecificLeftExpressionExpectation;
import com.loki2302.expectations.BinaryExpressionHasSpecificRightExpressionExpectation;
import com.loki2302.expectations.LiteralExpressionHasSpecificValueExpectation;
import com.loki2302.expectations.ExpressionIsBinaryExpressionExpectation;
import com.loki2302.expectations.ElementIsExpressionExpectation;
import com.loki2302.expectations.ExpressionIsLiteralExpressionExpectation;
import com.loki2302.expectations.ParseResultExpectation;
import com.loki2302.expectations.ParseResultIsBadExpectation;
import com.loki2302.expectations.ParseResultIsOkExpectation;
import com.loki2302.parser.AddSubExpressionParser;
import com.loki2302.parser.BoolExpressionParser;
import com.loki2302.parser.DoubleExpressionParser;
import com.loki2302.parser.ExpressionParser;
import com.loki2302.parser.IntExpressionParser;
import com.loki2302.parser.LiteralExpressionParser;
import com.loki2302.parser.MulDivExpressionParser;
import com.loki2302.parser.RootExpressionParser;

public class ParserTestDsl {
	private ParserTestDsl() {		
	}
	
	public static ParseResultExpectation result(ElementExpectation domElementDescriptor) {
		return new ParseResultIsOkExpectation(domElementDescriptor);
	}	
	
	public static ParseResultExpectation fail() {
		return new ParseResultIsBadExpectation();
	}
	
	public static ElementExpectation isExpression(ExpressionExpectation... expectations) {
		return new ElementIsExpressionExpectation(expectations);
	}
	
	public static ExpressionExpectation isLiteral(LiteralExpressionExpectation... expectations) {
		return new ExpressionIsLiteralExpressionExpectation(expectations);
	}
	
	public static LiteralExpressionExpectation ofType(DOMLiteralType literalType) {
		return new LiteralExpressionHasSpecificTypeExpectation(literalType);
	}
	
	public static LiteralExpressionExpectation havingValueOf(String stringValue) {
		return new LiteralExpressionHasSpecificValueExpectation(stringValue);
	}
	
	public static ExpressionExpectation isBinary(BinaryExpressionExpectation... expectations) {
		return new ExpressionIsBinaryExpressionExpectation(expectations);
	}
	
	public static BinaryExpressionExpectation ofType(DOMBinaryExpressionType expressionType) {
		return new BinaryExpressionHasSpecificTypeExpectation(expressionType);
	}
	
	public static ExpressionParser parseInt() {
		return new IntExpressionParser();
	}
	
	public static ExpressionParser parseDouble() {
		return new DoubleExpressionParser();
	}
	
	public static ExpressionParser parseBool() {
		return new BoolExpressionParser();
	}
	
	public static ExpressionParser parseLiteral() {
		return new LiteralExpressionParser();
	}
	
	public static ExpressionParser parseMulDiv() {
		return new MulDivExpressionParser();
	}
	
	public static ExpressionParser parseAddSub() {
		return new AddSubExpressionParser();
	}
	
	public static ExpressionParser parseExpression() {
	    return new RootExpressionParser();
	}
	
	public static BinaryExpressionExpectation withLeftExpression(ExpressionExpectation... leftExpressionExpectations) {
		return new BinaryExpressionHasSpecificLeftExpressionExpectation(leftExpressionExpectations);
	}
	
	public static BinaryExpressionExpectation withRightExpression(ExpressionExpectation... rightExpressionExpectations) {
		return new BinaryExpressionHasSpecificRightExpressionExpectation(rightExpressionExpectations);
	}	

    public static BinaryExpressionExpectation withIntLiteralAsLeftExpression(String stringValue) {
        return withLeftExpression(isLiteral(ofType(DOMLiteralType.Int), havingValueOf(stringValue)));
    }
    
    public static BinaryExpressionExpectation withIntLiteralAsRightExpression(String stringValue) {
        return withRightExpression(isLiteral(ofType(DOMLiteralType.Int), havingValueOf(stringValue)));
    }
}