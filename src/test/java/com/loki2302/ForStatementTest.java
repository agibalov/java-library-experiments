package com.loki2302;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.loki2302.expectations.element.expression.ExpressionExpectation;
import com.loki2302.expectations.element.statement.StatementExpectation;
import com.loki2302.expectations.element.statement.forstatement.ForStatementExpectation;
import com.loki2302.expectations.parser.ParseResultExpectation;
import com.loki2302.parser.ExpressionParser;
import static com.loki2302.expectations.ParserTestDsl.*;

@RunWith(Parameterized.class)
public class ForStatementTest {
	private final String expression;
	private final ExpressionParser parser;	
	private final ParseResultExpectation parseResultExpectation;
	
	public ForStatementTest(			
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
		
		parameters.add(new Object[] { " for ( ; ; ) ; ", parsePureStatement(), result(               
                isStatement(isFor(
                        hasNoInitializer(),
                        hasNoCondition(),
                        hasNoStep(),
                        hasNoBody()))
                ) });
		
		parameters.add(new Object[] { " for ( 1 ; ; ) ; ", parsePureStatement(), result(               
		        isStatement(isFor(
                        hasInitializer(),
                        hasNoCondition(),
                        hasNoStep(),
                        hasNoBody()))
                ) });
		
		parameters.add(new Object[] { " for ( ; 1 ; ) ; ", parsePureStatement(), result(               
		        isStatement(isFor(
                        hasNoInitializer(),
                        hasCondition(),
                        hasNoStep(),
                        hasNoBody()))
                ) });
		
		parameters.add(new Object[] { " for ( ; ; 1 ) ; ", parsePureStatement(), result(               
		        isStatement(isFor(
                        hasNoInitializer(),
                        hasNoCondition(),
                        hasStep(),
                        hasNoBody()))
                ) });
		
		parameters.add(new Object[] { " for ( ; ; ) 1 ; ", parsePureStatement(), result(               
		        isStatement(isFor(
                        hasNoInitializer(),
                        hasNoCondition(),
                        hasNoStep(),
                        hasBody()))
                ) });
		
		parameters.add(new Object[] { " for ( 1 ; 1 ; 1 ) 1 ; ", parsePureStatement(), result(               
		        isStatement(isFor(
		                hasInitializer(),
                        hasCondition(),
                        hasStep(),
                        hasBody()))
                ) });
		
		return parameters;
	}
	
	@Test
	public void testBinaryExpressionParseResult() {
		ParseResult parseResult = parser.parse(expression);
		parseResultExpectation.check(parseResult);
	}

	private static ForStatementExpectation hasInitializer(StatementExpectation... expectations) {
	    return hasInitializerStatement(expectations);
	}
	
	private static ForStatementExpectation hasNoInitializer() {
	    return hasInitializerStatement(isNullStatement());
	}
	
	private static ForStatementExpectation hasCondition(ExpressionExpectation... expectations) {
	    return forHasConditionExpression(expectations);
	}
	
	private static ForStatementExpectation hasNoCondition() {
        return forHasNoConditionExpression();
    }
	
	private static ForStatementExpectation hasStep(StatementExpectation... expectations) {
        return hasStepStatement(expectations);
    }
	
	private static ForStatementExpectation hasNoStep() {
	    return hasStepStatement(isNullStatement());
	}
	
	private static ForStatementExpectation hasBody(StatementExpectation... expectations) {
        return forHasBody(expectations);
    }
	
	private static ForStatementExpectation hasNoBody() {
        return forHasBody(isNullStatement());
    }
}