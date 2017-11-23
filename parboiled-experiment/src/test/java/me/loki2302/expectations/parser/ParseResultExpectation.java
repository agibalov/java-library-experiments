package me.loki2302.expectations.parser;

import me.loki2302.ParseResult;


public interface ParseResultExpectation {
	void check(ParseResult parseResult);
}