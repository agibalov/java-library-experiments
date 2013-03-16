package com.loki2302;

public class LiteralExpressionParser implements ExpressionParser {
	@Override
	public ParseResult parse(String expression) {
		Parser parser = new Parser();
		return parser.parseLiteral(expression);
	}		
}