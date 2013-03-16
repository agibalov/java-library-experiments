package com.loki2302;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.loki2302.expectations.ParseResultExpectation;

@RunWith(Parameterized.class)
public class LiteralTest extends ParserTestBase {
	private final String expression;
	private final ParseResultExpectation parseResultExpectation;
	
	public LiteralTest(
			String expression,
			ParseResultExpectation parseResultExpectation) {
		this.expression = expression;
		this.parseResultExpectation = parseResultExpectation;
	}
	
	@Parameters(name = "#{index}: Parse \"{0}\"")
	public static Collection<Object[]> makeTestData() {
		List<Object[]> parameters = new ArrayList<Object[]>();
		parameters.add(new Object[] {"123", result(isLiteral(havingTypeOf(DOMLiteralType.Int), havingStringValueOf("123"))) });
		parameters.add(new Object[] {" 123", result(isLiteral(havingTypeOf(DOMLiteralType.Int), havingStringValueOf("123"))) });
		parameters.add(new Object[] {"123 ", result(isLiteral(havingTypeOf(DOMLiteralType.Int), havingStringValueOf("123"))) });
		parameters.add(new Object[] {" 123 ", result(isLiteral(havingTypeOf(DOMLiteralType.Int), havingStringValueOf("123"))) });
		parameters.add(new Object[] {"abc", fail() });
		return parameters;
	}
	
	@Test
	public void testLiteralParseResult() {
		Parser parser = new Parser();
		ParseResult parseResult = parser.parseIntLiteral(expression);
		parseResultExpectation.check(parseResult);		
	}
}