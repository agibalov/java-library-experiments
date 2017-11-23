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
public class IfStatementTest {
	private final String expression;
	private final ExpressionParser parser;	
	private final ParseResultExpectation parseResultExpectation;
	
	public IfStatementTest(			
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
		
		parameters.add(new Object[] { " if ( 1 ) 2 ; ", parsePureStatement(), result(               
                isStatement(ifStatement(
                        withCondition(isIntLiteralWithValueOf("1")),
                        hasTrueBranch(isExpressionStatement(withExpression(isIntLiteralWithValueOf("2")))),
                        hasNoFalseBranch()))
                ) });
		
		parameters.add(new Object[] { " if ( 1 ) 2 ; else 3 ; ", parsePureStatement(), result(               
                isStatement(ifStatement(
                        withCondition(isIntLiteralWithValueOf("1")),
                        hasTrueBranch(isExpressionStatement(withExpression(isIntLiteralWithValueOf("2")))),
                        hasFalseBranch(isExpressionStatement(withExpression(isIntLiteralWithValueOf("3"))))))
                ) });
		
		return parameters;
	}
	
	@Test
	public void testBinaryExpressionParseResult() {
		ParseResult parseResult = parser.parse(expression);
		parseResultExpectation.check(parseResult);
	}	
}