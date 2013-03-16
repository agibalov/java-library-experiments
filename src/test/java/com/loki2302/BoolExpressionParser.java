package com.loki2302;

public class BoolExpressionParser implements ExpressionParser {
	@Override
	public ParseResult parse(String expression) {
		Parser parser = new Parser();
		return parser.parseBoolLiteral(expression);
	}		
}