package io.agibalov;

import io.agibalov.filterexpression.FilterLexer;
import io.agibalov.filterexpression.FilterParser;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.Value;
import org.antlr.v4.runtime.*;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class FilterTest {
    @Test
    public void positiveTest1() {
        assertEquals(
                Expression.builder()
                        .left("accountId")
                        .operation(Operation.NotEqual)
                        .right(StringLiteral.builder()
                                .text("qqq-www-123")
                                .build())
                        .build(),
                parseExpression("accountId!='qqq-www-123'"));
    }

    @Test
    public void positiveTest2() {
        assertEquals(
                Expression.builder()
                        .left("xxx")
                        .operation(Operation.LessOrEqual)
                        .right(IntegerLiteral.builder()
                                .value(123)
                                .build())
                        .build(),
                parseExpression("xxx<=123"));
    }

    @Test
    public void throwsIfExpressionIsNotValid() {
        try {
            parseExpression(" x ");
            fail();
        } catch (ParseException e) {
            assertEquals(
                    Arrays.asList(
                            "1:0 token recognition error at: ' '",
                            "1:2 token recognition error at: ' '"),
                    e.getLexerMessages());
            assertEquals(
                    Arrays.asList("1:3 mismatched input '<EOF>' expecting {'=', '!=', '<', '<=', '>', '>=', '~'}"),
                    e.getParserMessages());
        }
    }

    private static Expression parseExpression(String expressionString) {
        CollectingErrorListener lexerErrorListener = new CollectingErrorListener();
        FilterLexer lexer = new FilterLexer(CharStreams.fromString(expressionString));
        lexer.removeErrorListeners();
        lexer.addErrorListener(lexerErrorListener);

        CommonTokenStream tokenStream = new CommonTokenStream(lexer);

        CollectingErrorListener parserErrorListener = new CollectingErrorListener();
        FilterParser parser = new FilterParser(tokenStream);
        parser.removeErrorListeners();
        parser.addErrorListener(parserErrorListener);

        FilterParser.ExpressionContext expressionContext = parser.expression();
        List<String> lexerErrorMessages = lexerErrorListener.getMessages();
        List<String> parserErrorMessages = parserErrorListener.getMessages();
        if (!lexerErrorMessages.isEmpty() || !parserErrorMessages.isEmpty()) {
            throw new ParseException(lexerErrorMessages, parserErrorMessages);
        }

        return makeExpression(expressionContext);
    }

    private static class CollectingErrorListener extends BaseErrorListener {
        private final List<String> messages = new ArrayList<>();

        @Override
        public void syntaxError(
                Recognizer<?, ?> recognizer,
                Object offendingSymbol,
                int line,
                int charPositionInLine,
                String msg,
                RecognitionException e) {

            messages.add(String.format("%d:%d %s", line, charPositionInLine, msg));
        }

        public List<String> getMessages() {
            return Collections.unmodifiableList(messages);
        }
    }

    @ToString
    public static class ParseException extends RuntimeException {
        private final List<String> lexerMessages;
        private final List<String> parserMessages;

        public ParseException(List<String> lexerMessages, List<String> parserMessages) {
            this.lexerMessages = lexerMessages;
            this.parserMessages = parserMessages;
        }

        public List<String> getLexerMessages() {
            return Collections.unmodifiableList(lexerMessages);
        }

        public List<String> getParserMessages() {
            return Collections.unmodifiableList(parserMessages);
        }
    }

    private static Expression makeExpression(FilterParser.ExpressionContext expressionContext) {
        String left = expressionContext.left.getText();
        FilterParser.OperationContext operationContext = expressionContext.operation();
        Operation operation;
        if (operationContext.OP_EQUAL() != null) {
            operation = Operation.Equal;
        } else if (operationContext.OP_NOT_EQUAL() != null) {
            operation = Operation.NotEqual;
        } else if (operationContext.OP_LESS() != null) {
            operation = Operation.Less;
        } else if (operationContext.OP_LESS_OR_EQUAL() != null) {
            operation = Operation.LessOrEqual;
        } else if (operationContext.OP_GREATER() != null) {
            operation = Operation.Greater;
        } else if (operationContext.OP_GREATER_OR_EQUAL() != null) {
            operation = Operation.GreaterOrEqual;
        } else if (operationContext.OP_CONTAINS() != null) {
            operation = Operation.Contains;
        } else {
            throw new RuntimeException(String.format("Don't know how to handle %s", operationContext));
        }

        FilterParser.LiteralContext right = expressionContext.right;
        Literal literal;
        if (right.STRING_LITERAL() != null) {
            String text = right.STRING_LITERAL().getText();
            String unescapedText = StringEscapeUtils.unescapeJava(text.substring(1, text.length() - 1));
            literal = StringLiteral.builder()
                    .text(unescapedText)
                    .build();
        } else if (right.NUMBER_LITERAL() != null) {
            literal = IntegerLiteral.builder()
                    .value(Integer.valueOf(right.NUMBER_LITERAL().getText()))
                    .build();
        } else {
            throw new RuntimeException(String.format("Don't know how to handle %s", right));
        }

        return Expression.builder()
                .left(left)
                .operation(operation)
                .right(literal)
                .build();
    }

    @Value
    @Builder
    public static class Expression {
        private String left;
        private Operation operation;
        private Literal right;
    }

    public enum Operation {
        Equal,
        NotEqual,
        Less,
        LessOrEqual,
        Greater,
        GreaterOrEqual,
        Contains
    }

    public interface Literal {
    }

    @Data
    @Builder
    public static class StringLiteral implements Literal {
        private String text;
    }

    @Data
    @Builder
    public static class IntegerLiteral implements Literal {
        private int value;
    }
}
