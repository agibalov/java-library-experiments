package com.loki2302;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.loki2302.dom.DOMLiteralType;
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
		
		parameters.add(new Object[] {"123", parseInt(), result(isLiteral(ofType(DOMLiteralType.Int), havingValueOf("123"))) });
		parameters.add(new Object[] {" 123", parseInt(), result(isLiteral(ofType(DOMLiteralType.Int), havingValueOf("123"))) });
		parameters.add(new Object[] {"123 ", parseInt(), result(isLiteral(ofType(DOMLiteralType.Int), havingValueOf("123"))) });
		parameters.add(new Object[] {" 123 ", parseInt(), result(isLiteral(ofType(DOMLiteralType.Int), havingValueOf("123"))) });
		parameters.add(new Object[] {"abc", parseInt(), fail() });
		
		parameters.add(new Object[] {"3.14", parseDouble(), result(isLiteral(ofType(DOMLiteralType.Double), havingValueOf("3.14"))) });
		parameters.add(new Object[] {"3.", parseDouble(), result(isLiteral(ofType(DOMLiteralType.Double), havingValueOf("3."))) });
		parameters.add(new Object[] {".14", parseDouble(), result(isLiteral(ofType(DOMLiteralType.Double), havingValueOf(".14"))) });
		parameters.add(new Object[] {" 3.14", parseDouble(), result(isLiteral(ofType(DOMLiteralType.Double), havingValueOf("3.14"))) });
		parameters.add(new Object[] {"3.14 ", parseDouble(), result(isLiteral(ofType(DOMLiteralType.Double), havingValueOf("3.14"))) });
		parameters.add(new Object[] {" 3.14 ", parseDouble(), result(isLiteral(ofType(DOMLiteralType.Double), havingValueOf("3.14"))) });
		parameters.add(new Object[] {".", parseDouble(), fail() });
		parameters.add(new Object[] {"123", parseDouble(), fail() });
		parameters.add(new Object[] {"abc", parseDouble(), fail() });
		
		return parameters;
	}
	
	@Test
	public void testLiteralParseResult() {
		ParseResult parseResult = parser.parse(expression);
		parseResultExpectation.check(parseResult);
	}
	
	protected static ExpressionParser parseInt() {
		return new IntExpressionParser();
	}
	
	protected static ExpressionParser parseDouble() {
		return new DoubleExpressionParser();
	}
	
	public static interface ExpressionParser {
		ParseResult parse(String expression);
	}
	
	public static class IntExpressionParser implements ExpressionParser {
		@Override
		public ParseResult parse(String expression) {
			Parser parser = new Parser();
			return parser.parseIntLiteral(expression);
		}		
	}
	
	public static class DoubleExpressionParser implements ExpressionParser {
		@Override
		public ParseResult parse(String expression) {
			Parser parser = new Parser();
			return parser.parseDoubleLiteral(expression);
		}		
	}
}