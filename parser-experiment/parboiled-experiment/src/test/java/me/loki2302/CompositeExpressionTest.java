package me.loki2302;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.loki2302.ParseResult;
import me.loki2302.dom.DOMBinaryExpressionType;
import me.loki2302.expectations.parser.ParseResultExpectation;
import me.loki2302.parser.ExpressionParser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static me.loki2302.expectations.ParserTestDsl.*;

@RunWith(Parameterized.class)
public class CompositeExpressionTest {
	private final String expression;
	private final ExpressionParser parser;	
	private final ParseResultExpectation parseResultExpectation;
	
	public CompositeExpressionTest(			
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
		
		parameters.add(new Object[] { " 1 * 2 * 3 ", parseExpression(), result(
				
				isExpression(isBinary(
						ofType(DOMBinaryExpressionType.Mul),
						withLeftExpression(
								isBinary(
										ofType(DOMBinaryExpressionType.Mul),
										withIntLiteralAsLeftExpression("1"),
										withIntLiteralAsRightExpression("2"))),
						withIntLiteralAsRightExpression("3")))
				
				) });
		
		parameters.add(new Object[] { " 1 * 2 + 3 / 4 ", parseExpression(), result(
				
				isExpression(isBinary(
						ofType(DOMBinaryExpressionType.Add),
						withLeftExpression(
						        isBinary(
										ofType(DOMBinaryExpressionType.Mul),
										withIntLiteralAsLeftExpression("1"),
										withIntLiteralAsRightExpression("2"))),
						withRightExpression(
								isBinary(
										ofType(DOMBinaryExpressionType.Div),
										withIntLiteralAsLeftExpression("3"),
										withIntLiteralAsRightExpression("4")))))
												
				) });
		
		parameters.add(new Object[] { " (1 + 2) * 3 ", parseExpression(), result(
                
                isExpression(isBinary(
                        ofType(DOMBinaryExpressionType.Mul),
                        withLeftExpression(
                                isBinary(
                                        ofType(DOMBinaryExpressionType.Add),
                                        withIntLiteralAsLeftExpression("1"),
                                        withIntLiteralAsRightExpression("2"))),
                        withIntLiteralAsRightExpression("3")))
                
                ) });		
		
		return parameters;
	}
	
	@Test
	public void testBinaryExpressionParseResult() {
		ParseResult parseResult = parser.parse(expression);
		parseResultExpectation.check(parseResult);
	}	
}