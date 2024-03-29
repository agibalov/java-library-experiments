package me.loki2302;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.loki2302.ParseResult;
import me.loki2302.expectations.element.expression.ExpressionExpectation;
import me.loki2302.expectations.element.statement.StatementExpectation;
import me.loki2302.expectations.element.statement.forstatement.ForStatementExpectation;
import me.loki2302.expectations.parser.ParseResultExpectation;
import me.loki2302.parser.ExpressionParser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static me.loki2302.expectations.ParserTestDsl.*;

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
                        hasInitializer(isExpressionStatement(withExpression(isIntLiteralWithValueOf("1")))),
                        hasNoCondition(),
                        hasNoStep(),
                        hasNoBody()))
                ) });
		
		parameters.add(new Object[] { " for ( ; 1 ; ) ; ", parsePureStatement(), result(               
		        isStatement(isFor(
                        hasNoInitializer(),
                        hasCondition(isIntLiteralWithValueOf("1")),
                        hasNoStep(),
                        hasNoBody()))
                ) });
		
		parameters.add(new Object[] { " for ( ; ; 1 ) ; ", parsePureStatement(), result(               
		        isStatement(isFor(
                        hasNoInitializer(),
                        hasNoCondition(),
                        hasStep(isExpressionStatement(withExpression(isIntLiteralWithValueOf("1")))),
                        hasNoBody()))
                ) });
		
		parameters.add(new Object[] { " for ( ; ; ) 1 ; ", parsePureStatement(), result(               
		        isStatement(isFor(
                        hasNoInitializer(),
                        hasNoCondition(),
                        hasNoStep(),
                        hasBody(isExpressionStatement(withExpression(isIntLiteralWithValueOf("1"))))))
                ) });
		
		parameters.add(new Object[] { " for ( 1 ; 2 ; 3 ) 4 ; ", parsePureStatement(), result(               
		        isStatement(isFor(
		                hasInitializer(isExpressionStatement(withExpression(isIntLiteralWithValueOf("1")))),
                        hasCondition(isIntLiteralWithValueOf("2")),
                        hasStep(isExpressionStatement(withExpression(isIntLiteralWithValueOf("3")))),
                        hasBody(isExpressionStatement(withExpression(isIntLiteralWithValueOf("4"))))))
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