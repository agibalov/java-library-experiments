package com.loki2302;

import org.parboiled.BaseParser;
import org.parboiled.Rule;


public class Grammar extends BaseParser<DOMElement> {
	public Rule intLiteral() {
		return Sequence(
				optionalGap(),
				Sequence(
    				OneOrMore(CharRange('0', '9')),
    				push(new DOMLiteralExpression(
    						DOMLiteralType.Int, 
    						match()))),
    			optionalGap());
	}
	
	public Rule gap() {
		return String(" ");
	}
	
	public Rule optionalGap() {
		return ZeroOrMore(gap());
	}
	
	public Rule mandatoryGap() {
		return OneOrMore(gap());
	}
}