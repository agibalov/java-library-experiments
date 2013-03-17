package com.loki2302;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.loki2302.dom.DOMUnaryExpressionType;
import com.loki2302.expectations.ParseResultExpectation;
import com.loki2302.parser.ExpressionParser;
import static com.loki2302.ParserTestDsl.*;

@RunWith(Parameterized.class)
public class UnaryExpressionTest {
	private final String expression;
	private final ExpressionParser parser;	
	private final ParseResultExpectation parseResultExpectation;
	
	public UnaryExpressionTest(			
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
		
		parameters.add(new Object[] { " ++ 1 ", parseExpression(), result(               
                isExpression(isUnary(
                        ofType(DOMUnaryExpressionType.PrefixIncrement),
                        withIntLiteralAsInnerExpression("1")))                                
                ) });		
		
		parameters.add(new Object[] { " 1 ++ ", parseExpression(), result(               
                isExpression(isUnary(
                        ofType(DOMUnaryExpressionType.PostfixIncrement),
                        withIntLiteralAsInnerExpression("1")))                                
                ) });
		
		parameters.add(new Object[] { " -- 1 ", parseExpression(), result(               
                isExpression(isUnary(
                        ofType(DOMUnaryExpressionType.PrefixDecrement),
                        withIntLiteralAsInnerExpression("1")))                                
                ) });       
        
        parameters.add(new Object[] { " 1 -- ", parseExpression(), result(               
                isExpression(isUnary(
                        ofType(DOMUnaryExpressionType.PostfixDecrement),
                        withIntLiteralAsInnerExpression("1")))                                
                ) });
        
        parameters.add(new Object[] { " ! 1 ", parseExpression(), result(               
                isExpression(isUnary(
                        ofType(DOMUnaryExpressionType.Not),
                        withIntLiteralAsInnerExpression("1")))                                
                ) });
        
        parameters.add(new Object[] { " + 1 ", parseExpression(), result(               
                isExpression(isUnary(
                        ofType(DOMUnaryExpressionType.PlusSign),
                        withIntLiteralAsInnerExpression("1")))                                
                ) });
        
        parameters.add(new Object[] { " - 1 ", parseExpression(), result(               
                isExpression(isUnary(
                        ofType(DOMUnaryExpressionType.MinusSign),
                        withIntLiteralAsInnerExpression("1")))                                
                ) });
		
		return parameters;
	}
	
	@Test
	public void testBinaryExpressionParseResult() {
		ParseResult parseResult = parser.parse(expression);
		parseResultExpectation.check(parseResult);
	}	
}