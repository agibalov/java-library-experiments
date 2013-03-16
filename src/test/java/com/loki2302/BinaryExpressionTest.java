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
import com.loki2302.expectations.BinaryExpressionExpectation;
import com.loki2302.expectations.ElementExpectation;
import com.loki2302.expectations.ParseResultExpectation;
import com.loki2302.parser.ExpressionParser;
import static com.loki2302.ParserTestDsl.*;

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
		
		parameters.add(new Object[] { "1*2", parseMulDiv(), result(
				isBinaryMulExpression( 
						withIntLiteralAsLeftExpression("1"),
						withIntLiteralAsRightExpression("2"))) });
		
		parameters.add(new Object[] { " 1 * 2 ", parseMulDiv(), result(
				isBinaryMulExpression( 
						withIntLiteralAsLeftExpression("1"),
						withIntLiteralAsRightExpression("2"))) });
		
		parameters.add(new Object[] { "1/2", parseMulDiv(), result(
				isBinaryDivExpression( 
						withIntLiteralAsLeftExpression("1"),
						withIntLiteralAsRightExpression("2"))) });
		
		parameters.add(new Object[] { " 1 / 2 ", parseMulDiv(), result(
				isBinaryDivExpression( 
						withIntLiteralAsLeftExpression("1"),
						withIntLiteralAsRightExpression("2"))) });
		
		parameters.add(new Object[] { "1+2", parseAddSub(), result(
				isBinaryAddExpression( 
						withIntLiteralAsLeftExpression("1"),
						withIntLiteralAsRightExpression("2"))) });
		
		parameters.add(new Object[] { " 1 + 2 ", parseAddSub(), result(
				isBinaryAddExpression( 
						withIntLiteralAsLeftExpression("1"),
						withIntLiteralAsRightExpression("2"))) });
		
		parameters.add(new Object[] { "1-2", parseAddSub(), result(
				isBinarySubExpression( 
						withIntLiteralAsLeftExpression("1"),
						withIntLiteralAsRightExpression("2"))) });
		
		parameters.add(new Object[] { " 1 - 2 ", parseAddSub(), result(
				isBinarySubExpression( 
						withIntLiteralAsLeftExpression("1"),
						withIntLiteralAsRightExpression("2"))) });
				
		parameters.add(new Object[] { " 1 * 2 * 3 ", parseMulDiv(), result(
				
				isExpression(isBinary(
						ofType(DOMBinaryExpressionType.Mul),
						withLeftExpression(
								isBinary(
										ofType(DOMBinaryExpressionType.Mul),
										withLeftExpression(isLiteral(
												ofType(DOMLiteralType.Int),
												havingValueOf("1"))),
										withRightExpression(isLiteral(
												ofType(DOMLiteralType.Int),
												havingValueOf("2"))))),
						withRightExpression(
								isLiteral(
										ofType(DOMLiteralType.Int),
										havingValueOf("3")))))
				
				) });
		
		parameters.add(new Object[] { " 1 * 2 + 3 / 4 ", parseAddSub(), result(
				
				isExpression(isBinary(
						ofType(DOMBinaryExpressionType.Add),
						withLeftExpression(
						        isBinary(
										ofType(DOMBinaryExpressionType.Mul),
										withLeftExpression(isLiteral(
												ofType(DOMLiteralType.Int),
												havingValueOf("1"))),
										withRightExpression(isLiteral(
												ofType(DOMLiteralType.Int),
												havingValueOf("2"))))),
						withRightExpression(
								isBinary(
										ofType(DOMBinaryExpressionType.Div),
										withLeftExpression(isLiteral(
												ofType(DOMLiteralType.Int),
												havingValueOf("3"))),
										withRightExpression(isLiteral(
												ofType(DOMLiteralType.Int),
												havingValueOf("4")))))))
												
				) });
		
		parameters.add(new Object[] { " (1 + 2) * 3 ", parseExpression(), result(
                
                isExpression(isBinary(
                        ofType(DOMBinaryExpressionType.Mul),
                        withLeftExpression(
                                isBinary(
                                        ofType(DOMBinaryExpressionType.Add),
                                        withLeftExpression(isLiteral(
                                                ofType(DOMLiteralType.Int),
                                                havingValueOf("1"))),
                                        withRightExpression(isLiteral(
                                                ofType(DOMLiteralType.Int),
                                                havingValueOf("2"))))),
                        withRightExpression(
                                isLiteral(
                                        ofType(DOMLiteralType.Int),
                                        havingValueOf("3")))))
                
                ) });
		
		return parameters;
	}
	
	@Test
	public void testBinaryExpressionParseResult() {
		ParseResult parseResult = parser.parse(expression);
		parseResultExpectation.check(parseResult);
	}	
	
	private static ElementExpectation isBinaryMulExpression(BinaryExpressionExpectation... expectations) {		
		return isBinaryExpression(ArrayUtils.add(expectations, ofType(DOMBinaryExpressionType.Mul)));
	}
	
	private static ElementExpectation isBinaryDivExpression(BinaryExpressionExpectation... expectations) {		
		return isBinaryExpression(ArrayUtils.add(expectations, ofType(DOMBinaryExpressionType.Div)));
	}
	
	private static ElementExpectation isBinaryAddExpression(BinaryExpressionExpectation... expectations) {		
		return isBinaryExpression(ArrayUtils.add(expectations, ofType(DOMBinaryExpressionType.Add)));
	}
	
	private static ElementExpectation isBinarySubExpression(BinaryExpressionExpectation... expectations) {		
		return isBinaryExpression(ArrayUtils.add(expectations, ofType(DOMBinaryExpressionType.Sub)));
	}
	
	private static ElementExpectation isBinaryExpression(BinaryExpressionExpectation... expectations) {
		return isExpression(isBinary(expectations));
	}
	
	private static BinaryExpressionExpectation withIntLiteralAsLeftExpression(String stringValue) {
		return withLeftExpression(isLiteral(ofType(DOMLiteralType.Int), havingValueOf(stringValue)));
	}
	
	private static BinaryExpressionExpectation withIntLiteralAsRightExpression(String stringValue) {
		return withRightExpression(isLiteral(ofType(DOMLiteralType.Int), havingValueOf(stringValue)));
	}
}