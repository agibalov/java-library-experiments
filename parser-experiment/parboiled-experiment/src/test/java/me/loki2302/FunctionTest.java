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
public class FunctionTest {
	private final String expression;
	private final ExpressionParser parser;	
	private final ParseResultExpectation parseResultExpectation;
	
	public FunctionTest(			
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
		
		parameters.add(new Object[] { " int sum ( ) return 1; ", parseFunction(), result(               
                isFunction(
                        hasReturnType(isNamedTypeReference(withTypeName("int"))),
                        functionHasName("sum"),
                        functionHasNoParameters(),
                        hasBody(isReturn(hasExpression(isIntLiteralWithValueOf("1")))))
                ) });
		
		parameters.add(new Object[] { " int sum ( double x ) return 1; ", parseFunction(), result(               
                isFunction(
                        hasReturnType(isNamedTypeReference(withTypeName("int"))),
                        functionHasName("sum"),
                        hasParameters(1),
                        functionHasParameter(
                                0,
                                parameterHasName("x"), 
                                parameterHasType(isNamedTypeReference(withTypeName("double")))),
                        hasBody(isReturn(hasExpression(isIntLiteralWithValueOf("1")))))
                ) });
		
		parameters.add(new Object[] { " int sum ( double left , bool right ) return 1; ", parseFunction(), result(               
                isFunction(
                        hasReturnType(isNamedTypeReference(withTypeName("int"))),
                        functionHasName("sum"),
                        hasParameters(2),
                        functionHasParameter(
                                0,
                                parameterHasName("left"), 
                                parameterHasType(isNamedTypeReference(withTypeName("double")))),
                        functionHasParameter(
                                1,
                                parameterHasName("right"),
                                parameterHasType(isNamedTypeReference(withTypeName("bool")))),
                        hasBody(isReturn(hasExpression(isIntLiteralWithValueOf("1")))))
                ) });
		
		return parameters;
	}
	
	@Test
	public void testBinaryExpressionParseResult() {
		ParseResult parseResult = parser.parse(expression);
		parseResultExpectation.check(parseResult);
	}	
}