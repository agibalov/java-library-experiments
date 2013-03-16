package com.loki2302;

public class MulDivExpressionParser implements ExpressionParser {
	@Override
	public ParseResult parse(String expression) {
		Parser parser = new Parser();
		return parser.parseMulDiv(expression);
	}
}