package com.loki2302.expectations.parser;

import static org.junit.Assert.assertFalse;

import com.loki2302.ParseResult;


public class ParseResultIsBadExpectation implements ParseResultExpectation {
	@Override
	public void check(ParseResult parseResult) {
		assertFalse(parseResult.isOk());
	}		
}