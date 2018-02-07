package me.loki2302.parser;

import me.loki2302.ParseResult;

public interface ExpressionParser {
	ParseResult parse(String expression);
}