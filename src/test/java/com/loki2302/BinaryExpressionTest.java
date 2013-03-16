package com.loki2302;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.loki2302.dom.DOMBinaryExpressionType;
import com.loki2302.dom.DOMLiteralType;
import com.loki2302.expectations.DOMBinaryExpressionExpectation;
import com.loki2302.expectations.DOMElementExpectation;
import com.loki2302.expectations.ParseResultExpectation;

@RunWith(Parameterized.class)
public class BinaryExpressionTest extends ParserTestBase {
	private final String expression;
	private final ExpressionParser parser;	
	private final ParseResultExpectation parseResultExpectation;
	
	public BinaryExpressionTest(			
			String expression,
			ExpressionParser parser,
			ParseResultExpectation parseResultExpectation) {
		this.expression = expression;
		this.parser = parser;		
		this.parseResultExpectation = parseResultExpectation;
	}
	
	@Parameters(name = "#{index}: Parse \"{0}\"")
	public static Collection<Object[]> makeTestData() {
		List<Object[]> parameters = new ArrayList<Object[]>();
		
		parameters.add(new Object[] { "1*2", parseMulDiv(), result(
				isBinaryMulExpression( 
						havingLeftExpressionOfIntLiteral("1"),
						havingRightExpressionOfIntLiteral("2"))) });
		
		parameters.add(new Object[] { " 1 * 2 ", parseMulDiv(), result(
				isBinaryMulExpression( 
						havingLeftExpressionOfIntLiteral("1"),
						havingRightExpressionOfIntLiteral("2"))) });
		
		parameters.add(new Object[] { "1/2", parseMulDiv(), result(
				isBinaryDivExpression( 
						havingLeftExpressionOfIntLiteral("1"),
						havingRightExpressionOfIntLiteral("2"))) });
		
		parameters.add(new Object[] { " 1 / 2 ", parseMulDiv(), result(
				isBinaryDivExpression( 
						havingLeftExpressionOfIntLiteral("1"),
						havingRightExpressionOfIntLiteral("2"))) });
		
		parameters.add(new Object[] { "1+2", parseAddSub(), result(
				isBinaryAddExpression( 
						havingLeftExpressionOfIntLiteral("1"),
						havingRightExpressionOfIntLiteral("2"))) });
		
		parameters.add(new Object[] { " 1 + 2 ", parseAddSub(), result(
				isBinaryAddExpression( 
						havingLeftExpressionOfIntLiteral("1"),
						havingRightExpressionOfIntLiteral("2"))) });
		
		parameters.add(new Object[] { "1-2", parseAddSub(), result(
				isBinarySubExpression( 
						havingLeftExpressionOfIntLiteral("1"),
						havingRightExpressionOfIntLiteral("2"))) });
		
		parameters.add(new Object[] { " 1 - 2 ", parseAddSub(), result(
				isBinarySubExpression( 
						havingLeftExpressionOfIntLiteral("1"),
						havingRightExpressionOfIntLiteral("2"))) });
		
		// TODO: add tests like "1*2*3"
		// TODO: add tests like "1*2+3*4"
		
		return parameters;
	}
	
	@Test
	public void testBinaryExpressionParseResult() {
		ParseResult parseResult = parser.parse(expression);
		parseResultExpectation.check(parseResult);
	}	
	
	private static DOMElementExpectation isBinaryMulExpression(DOMBinaryExpressionExpectation... expectations) {		
		return isBinaryExpression(ArrayUtils.add(expectations, ofType(DOMBinaryExpressionType.Mul)));
	}
	
	private static DOMElementExpectation isBinaryDivExpression(DOMBinaryExpressionExpectation... expectations) {		
		return isBinaryExpression(ArrayUtils.add(expectations, ofType(DOMBinaryExpressionType.Div)));
	}
	
	private static DOMElementExpectation isBinaryAddExpression(DOMBinaryExpressionExpectation... expectations) {		
		return isBinaryExpression(ArrayUtils.add(expectations, ofType(DOMBinaryExpressionType.Add)));
	}
	
	private static DOMElementExpectation isBinarySubExpression(DOMBinaryExpressionExpectation... expectations) {		
		return isBinaryExpression(ArrayUtils.add(expectations, ofType(DOMBinaryExpressionType.Sub)));
	}
	
	private static DOMElementExpectation isBinaryExpression(DOMBinaryExpressionExpectation... expectations) {
		return isExpression(isBinary(expectations));
	}
	
	private static DOMBinaryExpressionExpectation havingLeftExpressionOfIntLiteral(String stringValue) {
		return havingLeftExpression(isLiteral(ofType(DOMLiteralType.Int), havingValueOf(stringValue)));
	}
	
	private static DOMBinaryExpressionExpectation havingRightExpressionOfIntLiteral(String stringValue) {
		return havingRightExpression(isLiteral(ofType(DOMLiteralType.Int), havingValueOf(stringValue)));
	}
}