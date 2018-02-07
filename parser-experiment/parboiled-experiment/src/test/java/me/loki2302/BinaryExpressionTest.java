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
public class BinaryExpressionTest {
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
		
		parameters.add(new Object[] { " 1 + 2 ", parseExpression(), result(               
                isExpression(isBinary(
                        ofType(DOMBinaryExpressionType.Add),
                        withIntLiteralAsLeftExpression("1"),
                        withIntLiteralAsRightExpression("2")))                                
                ) });
		
		parameters.add(new Object[] { " 1 - 2 ", parseExpression(), result(               
                isExpression(isBinary(
                        ofType(DOMBinaryExpressionType.Sub),
                        withIntLiteralAsLeftExpression("1"),
                        withIntLiteralAsRightExpression("2")))                                
                ) });
		
		parameters.add(new Object[] { " 1 * 2 ", parseExpression(), result(               
                isExpression(isBinary(
                        ofType(DOMBinaryExpressionType.Mul),
                        withIntLiteralAsLeftExpression("1"),
                        withIntLiteralAsRightExpression("2")))                                
                ) });
		
		parameters.add(new Object[] { " 1 / 2 ", parseExpression(), result(               
                isExpression(isBinary(
                        ofType(DOMBinaryExpressionType.Div),
                        withIntLiteralAsLeftExpression("1"),
                        withIntLiteralAsRightExpression("2")))                                
                ) });
		
		parameters.add(new Object[] { " 1 < 2 ", parseExpression(), result(               
                isExpression(isBinary(
                        ofType(DOMBinaryExpressionType.Less),
                        withIntLiteralAsLeftExpression("1"),
                        withIntLiteralAsRightExpression("2")))                                
                ) });
		
		parameters.add(new Object[] { " 1 <= 2 ", parseExpression(), result(               
                isExpression(isBinary(
                        ofType(DOMBinaryExpressionType.LessOrEqual),
                        withIntLiteralAsLeftExpression("1"),
                        withIntLiteralAsRightExpression("2")))                                
                ) });
		
		parameters.add(new Object[] { " 1 > 2 ", parseExpression(), result(               
                isExpression(isBinary(
                        ofType(DOMBinaryExpressionType.Greater),
                        withIntLiteralAsLeftExpression("1"),
                        withIntLiteralAsRightExpression("2")))                                
                ) });
		
		parameters.add(new Object[] { " 1 >= 2 ", parseExpression(), result(               
                isExpression(isBinary(
                        ofType(DOMBinaryExpressionType.GreaterOrEqual),
                        withIntLiteralAsLeftExpression("1"),
                        withIntLiteralAsRightExpression("2")))                                
                ) });
		
		parameters.add(new Object[] { " 1 == 2 ", parseExpression(), result(               
                isExpression(isBinary(
                        ofType(DOMBinaryExpressionType.Equal),
                        withIntLiteralAsLeftExpression("1"),
                        withIntLiteralAsRightExpression("2")))                                
                ) });
		
		parameters.add(new Object[] { " 1 != 2 ", parseExpression(), result(               
                isExpression(isBinary(
                        ofType(DOMBinaryExpressionType.NotEqual),
                        withIntLiteralAsLeftExpression("1"),
                        withIntLiteralAsRightExpression("2")))                                
                ) });
		
		parameters.add(new Object[] { " 1 += 2 ", parseExpression(), result(               
                isExpression(isBinary(
                        ofType(DOMBinaryExpressionType.AddAndAssign),
                        withIntLiteralAsLeftExpression("1"),
                        withIntLiteralAsRightExpression("2")))                                
                ) });
		
		parameters.add(new Object[] { " 1 -= 2 ", parseExpression(), result(               
                isExpression(isBinary(
                        ofType(DOMBinaryExpressionType.SubAndAssign),
                        withIntLiteralAsLeftExpression("1"),
                        withIntLiteralAsRightExpression("2")))                                
                ) });
		
		parameters.add(new Object[] { " 1 *= 2 ", parseExpression(), result(               
                isExpression(isBinary(
                        ofType(DOMBinaryExpressionType.MulAndAssign),
                        withIntLiteralAsLeftExpression("1"),
                        withIntLiteralAsRightExpression("2")))                                
                ) });
		
		parameters.add(new Object[] { " 1 /= 2 ", parseExpression(), result(               
                isExpression(isBinary(
                        ofType(DOMBinaryExpressionType.DivAndAssign),
                        withIntLiteralAsLeftExpression("1"),
                        withIntLiteralAsRightExpression("2")))                                
                ) });
		
		parameters.add(new Object[] { " 1 && 2 ", parseExpression(), result(               
                isExpression(isBinary(
                        ofType(DOMBinaryExpressionType.And),
                        withIntLiteralAsLeftExpression("1"),
                        withIntLiteralAsRightExpression("2")))                                
                ) });
		
		parameters.add(new Object[] { " 1 || 2 ", parseExpression(), result(               
                isExpression(isBinary(
                        ofType(DOMBinaryExpressionType.Or),
                        withIntLiteralAsLeftExpression("1"),
                        withIntLiteralAsRightExpression("2")))                                
                ) });		
		
		parameters.add(new Object[] { " 1 = 2 ", parseExpression(), result(               
                isExpression(isBinary(
                        ofType(DOMBinaryExpressionType.Assignment),
                        withIntLiteralAsLeftExpression("1"),
                        withIntLiteralAsRightExpression("2")))                                
                ) });
		
		return parameters;
	}
	
	@Test
	public void testBinaryExpressionParseResult() {
		ParseResult parseResult = parser.parse(expression);
		parseResultExpectation.check(parseResult);
	}	
}