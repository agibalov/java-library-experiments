package com.loki2302;

public class AddSubExpressionParser implements ExpressionParser {
	@Override
	public ParseResult parse(String expression) {
		Parser parser = new Parser();
		return parser.parseAddSub(expression);
	}
}