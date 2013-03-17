package com.loki2302.expectations.parser;

import com.loki2302.ParseResult;


public interface ParseResultExpectation {
	void check(ParseResult parseResult);
}