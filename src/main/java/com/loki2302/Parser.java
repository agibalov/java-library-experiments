package com.loki2302;

import org.parboiled.Parboiled;
import org.parboiled.parserunners.ParseRunner;
import org.parboiled.parserunners.RecoveringParseRunner;
import org.parboiled.support.ParsingResult;

import com.loki2302.dom.DOMElement;


public class Parser {
	private final Grammar grammar = Parboiled.createParser(Grammar.class);
	
	public ParseResult parseIntLiteral(String expression) {
		ParseRunner<DOMElement> parseRunner = new RecoveringParseRunner<DOMElement>(grammar.intLiteral());
		ParsingResult<DOMElement> parsingResult = parseRunner.run(expression);
		if(parsingResult.hasErrors() || !parsingResult.matched) {
			return ParseResult.fail();
		}
		
		return ParseResult.ok(parsingResult.resultValue);
	}
	
	public ParseResult parseDoubleLiteral(String expression) {
		ParseRunner<DOMElement> parseRunner = new RecoveringParseRunner<DOMElement>(grammar.doubleLiteral());
		ParsingResult<DOMElement> parsingResult = parseRunner.run(expression);
		if(parsingResult.hasErrors() || !parsingResult.matched) {
			return ParseResult.fail();
		}
		
		return ParseResult.ok(parsingResult.resultValue);
	}
}