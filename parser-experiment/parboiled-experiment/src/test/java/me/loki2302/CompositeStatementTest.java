package me.loki2302;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.loki2302.ParseResult;
import me.loki2302.expectations.parser.ParseResultExpectation;
import me.loki2302.parser.ExpressionParser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static me.loki2302.expectations.ParserTestDsl.*;

@RunWith(Parameterized.class)
public class CompositeStatementTest {
	private final String expression;
	private final ExpressionParser parser;	
	private final ParseResultExpectation parseResultExpectation;
	
	public CompositeStatementTest(			
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
		
		parameters.add(new Object[] { " { 1; 2; } ", parsePureStatement(), result(               
                isStatement(isCompositeStatement(
                        withChildren(2),
                        withChild(0, isExpressionStatement(withExpression(isLiteral(havingValueOf("1"))))),
                        withChild(1, isExpressionStatement(withExpression(isLiteral(havingValueOf("2")))))))
                ) });
		
		return parameters;
	}
	
	@Test
	public void testBinaryExpressionParseResult() {
		ParseResult parseResult = parser.parse(expression);
		parseResultExpectation.check(parseResult);
	}	
}