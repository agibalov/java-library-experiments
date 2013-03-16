package com.loki2302.parser;

import com.loki2302.ParseResult;

public interface ExpressionParser {
	ParseResult parse(String expression);
}