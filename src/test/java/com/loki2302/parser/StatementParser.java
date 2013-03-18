package com.loki2302.parser;

import com.loki2302.ParseResult;
import com.loki2302.Parser;

public class StatementParser implements ExpressionParser {
    @Override
    public ParseResult parse(String expression) {
        Parser parser = new Parser();
        return parser.parseStatement(expression);
    }
}