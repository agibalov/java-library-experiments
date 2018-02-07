package me.loki2302;

import me.loki2302.calcgrammar.CalculatorBaseVisitor;
import me.loki2302.calcgrammar.CalculatorLexer;
import me.loki2302.calcgrammar.CalculatorParser;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;
import org.mockito.MockingDetails;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class AppTest {
    @Test
    public void canGetLexerError() {
        ANTLRErrorListener lexerErrorListener = mock(ANTLRErrorListener.class);

        CharStream charStream = CharStreams.fromString(" x ");
        CalculatorLexer calculatorLexer = new CalculatorLexer(charStream);
        calculatorLexer.removeErrorListeners();
        calculatorLexer.addErrorListener(lexerErrorListener);

        CommonTokenStream commonTokenStream = new CommonTokenStream(calculatorLexer);
        CalculatorParser calculatorParser = new CalculatorParser(commonTokenStream);
        calculatorParser.program();

        MockingDetails mockingDetails = Mockito.mockingDetails(lexerErrorListener);
        Invocation invocation = mockingDetails.getInvocations().iterator().next();
        assertEquals(1, (int)invocation.<Integer>getArgument(2));
        assertEquals(1, (int)invocation.<Integer>getArgument(3));
        assertEquals("token recognition error at: 'x'", invocation.<Integer>getArgument(4));
    }

    @Test
    public void canGetParserError() {
        ANTLRErrorListener parserErrorListener = mock(ANTLRErrorListener.class);

        CharStream charStream = CharStreams.fromString(" 2 3 ");
        CalculatorLexer calculatorLexer = new CalculatorLexer(charStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(calculatorLexer);
        CalculatorParser calculatorParser = new CalculatorParser(commonTokenStream);
        calculatorParser.removeErrorListeners();
        calculatorParser.addErrorListener(parserErrorListener);
        calculatorParser.program();

        MockingDetails mockingDetails = Mockito.mockingDetails(parserErrorListener);
        Invocation invocation = mockingDetails.getInvocations().iterator().next();
        assertEquals(1, (int)invocation.<Integer>getArgument(2));
        assertEquals(3, (int)invocation.<Integer>getArgument(3));
        assertEquals("extraneous input '3' expecting <EOF>", invocation.<Integer>getArgument(4));
    }

    @Test
    public void canCalculateTheExpression() {
        CharStream charStream = CharStreams.fromString(" 2 + 3 \n* ( 4 - 5 )");
        CalculatorLexer calculatorLexer = new CalculatorLexer(charStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(calculatorLexer);
        CalculatorParser calculatorParser = new CalculatorParser(commonTokenStream);
        CalculatorParser.ProgramContext programContext = calculatorParser.program();

        CalculatingCalculatorParseTreeVisitor calculatingCalculatorParseTreeVisitor =
                new CalculatingCalculatorParseTreeVisitor();
        int result = programContext.accept(calculatingCalculatorParseTreeVisitor);
        assertEquals(-1, result);
    }

    public static class CalculatingCalculatorParseTreeVisitor extends CalculatorBaseVisitor<Integer> {
        @Override
        public Integer visitProgram(CalculatorParser.ProgramContext ctx) {
            return ctx.expression().accept(this);
        }

        @Override
        public Integer visitOperationExpression(CalculatorParser.OperationExpressionContext ctx) {
            int leftValue = ctx.left.accept(this);
            int rightValue = ctx.right.accept(this);

            int operationType = ctx.operation.getType();
            if(operationType == CalculatorParser.OP_ADD) {
                return leftValue + rightValue;
            } else if(operationType == CalculatorParser.OP_SUB) {
                return leftValue - rightValue;
            } else if(operationType == CalculatorParser.OP_MUL) {
                return leftValue * rightValue;
            } else if(operationType == CalculatorParser.OP_DIV) {
                return leftValue / rightValue;
            }

            throw new RuntimeException("Unknown operation " + ctx.operation.getText());
        }

        @Override
        public Integer visitAtomExpression(CalculatorParser.AtomExpressionContext ctx) {
            return Integer.valueOf(ctx.atom.getText());
        }

        @Override
        public Integer visitParenthesesExpression(CalculatorParser.ParenthesesExpressionContext ctx) {
            return ctx.expression().accept(this);
        }
    }
}
