package com.loki2302;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.loki2302.dom.DOMLiteralType;
import com.loki2302.expectations.DOMElementExpectation;
import com.loki2302.expectations.DOMLiteralExpressionExpectation;
import com.loki2302.expectations.ParseResultExpectation;

@RunWith(Parameterized.class)
public class LiteralTest extends ParserTestBase {
	private final String expression;
	private final ExpressionParser parser;	
	private final ParseResultExpectation parseResultExpectation;
	
	public LiteralTest(			
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
		
		parameters.add(new Object[] {"123", parseInt(), result(isLiteralExpression(ofType(DOMLiteralType.Int), havingValueOf("123"))) });
		parameters.add(new Object[] {" 123", parseInt(), result(isLiteralExpression(ofType(DOMLiteralType.Int), havingValueOf("123"))) });
		parameters.add(new Object[] {"123 ", parseInt(), result(isLiteralExpression(ofType(DOMLiteralType.Int), havingValueOf("123"))) });
		parameters.add(new Object[] {" 123 ", parseInt(), result(isLiteralExpression(ofType(DOMLiteralType.Int), havingValueOf("123"))) });
		parameters.add(new Object[] {"abc", parseInt(), fail() });
		
		parameters.add(new Object[] {"3.14", parseDouble(), result(isLiteralExpression(ofType(DOMLiteralType.Double), havingValueOf("3.14"))) });
		parameters.add(new Object[] {"3.", parseDouble(), result(isLiteralExpression(ofType(DOMLiteralType.Double), havingValueOf("3."))) });
		parameters.add(new Object[] {".14", parseDouble(), result(isLiteralExpression(ofType(DOMLiteralType.Double), havingValueOf(".14"))) });
		parameters.add(new Object[] {" 3.14", parseDouble(), result(isLiteralExpression(ofType(DOMLiteralType.Double), havingValueOf("3.14"))) });
		parameters.add(new Object[] {"3.14 ", parseDouble(), result(isLiteralExpression(ofType(DOMLiteralType.Double), havingValueOf("3.14"))) });
		parameters.add(new Object[] {" 3.14 ", parseDouble(), result(isLiteralExpression(ofType(DOMLiteralType.Double), havingValueOf("3.14"))) });
		parameters.add(new Object[] {".", parseDouble(), fail() });
		parameters.add(new Object[] {"123", parseDouble(), fail() });
		parameters.add(new Object[] {"abc", parseDouble(), fail() });
		
		parameters.add(new Object[] {"true", parseBool(), result(isLiteralExpression(ofType(DOMLiteralType.Bool), havingValueOf("true"))) });
		parameters.add(new Object[] {"false", parseBool(), result(isLiteralExpression(ofType(DOMLiteralType.Bool), havingValueOf("false"))) });
		parameters.add(new Object[] {" true", parseBool(), result(isLiteralExpression(ofType(DOMLiteralType.Bool), havingValueOf("true"))) });
		parameters.add(new Object[] {"true ", parseBool(), result(isLiteralExpression(ofType(DOMLiteralType.Bool), havingValueOf("true"))) });
		parameters.add(new Object[] {" true ", parseBool(), result(isLiteralExpression(ofType(DOMLiteralType.Bool), havingValueOf("true"))) });
		parameters.add(new Object[] {" false", parseBool(), result(isLiteralExpression(ofType(DOMLiteralType.Bool), havingValueOf("false"))) });
		parameters.add(new Object[] {"false ", parseBool(), result(isLiteralExpression(ofType(DOMLiteralType.Bool), havingValueOf("false"))) });
		parameters.add(new Object[] {" false ", parseBool(), result(isLiteralExpression(ofType(DOMLiteralType.Bool), havingValueOf("false"))) });
		parameters.add(new Object[] {"abc", parseBool(), fail() });
		
		parameters.add(new Object[] {"true", parseLiteral(), result(isLiteralExpression(ofType(DOMLiteralType.Bool), havingValueOf("true"))) });
		parameters.add(new Object[] {"123", parseLiteral(), result(isLiteralExpression(ofType(DOMLiteralType.Int), havingValueOf("123"))) });
		parameters.add(new Object[] {"123.", parseLiteral(), result(isLiteralExpression(ofType(DOMLiteralType.Double), havingValueOf("123."))) });
		parameters.add(new Object[] {"abc", parseLiteral(), fail() });
		
		return parameters;
	}
	
	@Test
	public void testLiteralParseResult() {
		ParseResult parseResult = parser.parse(expression);
		parseResultExpectation.check(parseResult);
	}	
	
	private static DOMElementExpectation isLiteralExpression(DOMLiteralExpressionExpectation... expectations) {
		return isExpression(isLiteral(expectations));
	}
}