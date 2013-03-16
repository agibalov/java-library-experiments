package com.loki2302;


public class DoubleExpressionParser implements ExpressionParser {
	@Override
	public ParseResult parse(String expression) {
		Parser parser = new Parser();
		return parser.parseDoubleLiteral(expression);
	}		
}