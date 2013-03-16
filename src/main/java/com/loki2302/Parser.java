package com.loki2302;

import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.parserunners.ParseRunner;
import org.parboiled.parserunners.RecoveringParseRunner;
import org.parboiled.support.ParsingResult;

import com.loki2302.dom.DOMElement;

public class Parser {
	private final Grammar grammar = Parboiled.createParser(Grammar.class);
	
	public ParseResult parseIntLiteral(String expression) {
		return parseExpressionWithRule(expression, grammar.intLiteral());
	}
	
	public ParseResult parseDoubleLiteral(String expression) {
		return parseExpressionWithRule(expression, grammar.doubleLiteral());
	}
	
	public ParseResult parseBoolLiteral(String expression) {
		return parseExpressionWithRule(expression, grammar.boolLiteral());
	}
	
	public ParseResult parseLiteral(String expression) {
		return parseExpressionWithRule(expression, grammar.literal());
	}
	
	public ParseResult parseMulDiv(String expression) {
		return parseExpressionWithRule(expression, grammar.mulDivExpression());
	}
	
	public ParseResult parseAddSub(String expression) {
		return parseExpressionWithRule(expression, grammar.addSubExpression());
	}
	
	public ParseResult parseExpression(String expression) {
        return parseExpressionWithRule(expression, grammar.expression());
    }
	
	private ParseResult parseExpressionWithRule(String expression, Rule rule) {
		ParseRunner<DOMElement> parseRunner = new RecoveringParseRunner<DOMElement>(rule);
		ParsingResult<DOMElement> parsingResult = parseRunner.run(expression);
		if(parsingResult.hasErrors() || !parsingResult.matched) {
			return ParseResult.fail();
		}
		
		return ParseResult.ok(parsingResult.resultValue);
	}
}