package com.loki2302;

public class IntExpressionParser implements ExpressionParser {
	@Override
	public ParseResult parse(String expression) {
		Parser parser = new Parser();
		return parser.parseIntLiteral(expression);
	}
}