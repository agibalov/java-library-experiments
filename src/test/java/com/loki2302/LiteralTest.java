package com.loki2302;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.loki2302.dom.DOMLiteralType;
import com.loki2302.expectations.element.ElementExpectation;
import com.loki2302.expectations.element.expression.literal.LiteralExpressionExpectation;
import com.loki2302.expectations.parser.ParseResultExpectation;
import com.loki2302.parser.ExpressionParser;
import static com.loki2302.expectations.ParserTestDsl.*;

@RunWith(Parameterized.class)
public class LiteralTest {
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
		
		parameters.add(new Object[] {"123", parseExpression(), result(isLiteralExpression(ofType(DOMLiteralType.Int), havingValueOf("123"))) });
		parameters.add(new Object[] {" 123", parseExpression(), result(isLiteralExpression(ofType(DOMLiteralType.Int), havingValueOf("123"))) });
		parameters.add(new Object[] {"123 ", parseExpression(), result(isLiteralExpression(ofType(DOMLiteralType.Int), havingValueOf("123"))) });
		parameters.add(new Object[] {" 123 ", parseExpression(), result(isLiteralExpression(ofType(DOMLiteralType.Int), havingValueOf("123"))) });
		
		parameters.add(new Object[] {"3.14", parseExpression(), result(isLiteralExpression(ofType(DOMLiteralType.Double), havingValueOf("3.14"))) });
		parameters.add(new Object[] {"3.", parseExpression(), result(isLiteralExpression(ofType(DOMLiteralType.Double), havingValueOf("3."))) });
		parameters.add(new Object[] {".14", parseExpression(), result(isLiteralExpression(ofType(DOMLiteralType.Double), havingValueOf(".14"))) });
		parameters.add(new Object[] {" 3.14", parseExpression(), result(isLiteralExpression(ofType(DOMLiteralType.Double), havingValueOf("3.14"))) });
		parameters.add(new Object[] {"3.14 ", parseExpression(), result(isLiteralExpression(ofType(DOMLiteralType.Double), havingValueOf("3.14"))) });
		parameters.add(new Object[] {" 3.14 ", parseExpression(), result(isLiteralExpression(ofType(DOMLiteralType.Double), havingValueOf("3.14"))) });
		parameters.add(new Object[] {".", parseExpression(), fail() });
		
		parameters.add(new Object[] {"true", parseExpression(), result(isLiteralExpression(ofType(DOMLiteralType.Bool), havingValueOf("true"))) });
		parameters.add(new Object[] {"false", parseExpression(), result(isLiteralExpression(ofType(DOMLiteralType.Bool), havingValueOf("false"))) });
		parameters.add(new Object[] {" true", parseExpression(), result(isLiteralExpression(ofType(DOMLiteralType.Bool), havingValueOf("true"))) });
		parameters.add(new Object[] {"true ", parseExpression(), result(isLiteralExpression(ofType(DOMLiteralType.Bool), havingValueOf("true"))) });
		parameters.add(new Object[] {" true ", parseExpression(), result(isLiteralExpression(ofType(DOMLiteralType.Bool), havingValueOf("true"))) });
		parameters.add(new Object[] {" false", parseExpression(), result(isLiteralExpression(ofType(DOMLiteralType.Bool), havingValueOf("false"))) });
		parameters.add(new Object[] {"false ", parseExpression(), result(isLiteralExpression(ofType(DOMLiteralType.Bool), havingValueOf("false"))) });
		parameters.add(new Object[] {" false ", parseExpression(), result(isLiteralExpression(ofType(DOMLiteralType.Bool), havingValueOf("false"))) });
		
		parameters.add(new Object[] {"true", parseExpression(), result(isLiteralExpression(ofType(DOMLiteralType.Bool), havingValueOf("true"))) });
		parameters.add(new Object[] {"123", parseExpression(), result(isLiteralExpression(ofType(DOMLiteralType.Int), havingValueOf("123"))) });
		parameters.add(new Object[] {"123.", parseExpression(), result(isLiteralExpression(ofType(DOMLiteralType.Double), havingValueOf("123."))) });
		
		return parameters;
	}
	
	@Test
	public void testLiteralParseResult() {
		ParseResult parseResult = parser.parse(expression);
		parseResultExpectation.check(parseResult);
	}	
	
	private static ElementExpectation isLiteralExpression(LiteralExpressionExpectation... expectations) {
		return isExpression(isLiteral(expectations));
	}
}