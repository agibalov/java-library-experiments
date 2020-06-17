package io.agibalov;

import io.agibalov.calcgrammar.CalculatorBaseVisitor;
import io.agibalov.calcgrammar.CalculatorLexer;
import io.agibalov.calcgrammar.CalculatorParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CalculatorTest {
    @Test
    public void canGetErrors() {
        CalculatorLexer lexer = new CalculatorLexer(CharStreams.fromString("  2 3 "));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        CalculatorParser parser = new CalculatorParser(tokenStream);
        parser.program();
        assertEquals(1, parser.getNumberOfSyntaxErrors());
    }

    @Test
    public void canCalculateExpression() {
        CalculatorLexer lexer = new CalculatorLexer(CharStreams.fromString("  2 + 3 \\n* ( 4 - 5 ) "));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        CalculatorParser parser = new CalculatorParser(tokenStream);
        CalculatorParser.ProgramContext programContext = parser.program();
        assertEquals(0, parser.getNumberOfSyntaxErrors());

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
