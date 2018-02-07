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
public class SpacesTest {
	private final String expression;
	private final ExpressionParser parser;	
	private final ParseResultExpectation parseResultExpectation;
	
	public SpacesTest(			
			String expression,
			ExpressionParser parser,
			ParseResultExpectation parseResultExpectation) {
		this.expression = expression;
		this.parser = parser;		
		this.parseResultExpectation = parseResultExpectation;
	}
	
	/*
	
	Looks like there's a bug in Eclipse: it shows no test runs when
	name has \n characters
	
	*/
	
	@Parameters/*(name = "#{index}: Parse \"{0}\"")*/
	public static Collection<Object[]> makeTestData() {
		List<Object[]> parameters = new ArrayList<Object[]>();
		
		ParseResultExpectation expectation = result(
		        isExpression(isBinary(
                        ofType(DOMBinaryExpressionType.Add),
                        withIntLiteralAsLeftExpression("1"),
                        withIntLiteralAsRightExpression("2")))                                
                );
		
		parameters.add(new Object[] { "1+2", parseExpression(), expectation });
		parameters.add(new Object[] { " 1 + 2 ", parseExpression(), expectation });
		parameters.add(new Object[] { "/**/1/*comment*/+/*another*/2/*end*/", parseExpression(), expectation });
		parameters.add(new Object[] { "\t1\t+\t2\t", parseExpression(), expectation });
		parameters.add(new Object[] { "\n1\n+\n2\n", parseExpression(), expectation });	
		parameters.add(new Object[] { "1 // line 1\n+// line 2\n2 // line 3\n", parseExpression(), expectation });
		parameters.add(new Object[] { "\n\t/* general\ncomment\nhere*/\t1/*more*/ // line 1\n+// line 2\n2 // line 3\n", 
		        parseExpression(), expectation });
		
		return parameters;
	}
	
	@Test
	public void testBinaryExpressionParseResult() {
		ParseResult parseResult = parser.parse(expression);		
		parseResultExpectation.check(parseResult);		
	}	
}