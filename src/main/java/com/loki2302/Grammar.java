package com.loki2302;

import org.parboiled.BaseParser;
import org.parboiled.Rule;

import com.loki2302.dom.DOMElement;
import com.loki2302.dom.DOMLiteralExpression;
import com.loki2302.dom.DOMLiteralType;


public class Grammar extends BaseParser<DOMElement> {
	public Rule intLiteral() {
		return Sequence(
				optionalGap(),
				Sequence(
    				OneOrMore(CharRange('0', '9')),
    				push(new DOMLiteralExpression(DOMLiteralType.Int, match()))),
    			optionalGap());
	}
	
	public Rule doubleLiteral() {
		return Sequence(
				optionalGap(),
				Sequence(
						FirstOf(
								Sequence(".", OneOrMore(CharRange('0', '9'))),
								Sequence(OneOrMore(CharRange('0', '9')), ".", OneOrMore(CharRange('0', '9'))),
								Sequence(OneOrMore(CharRange('0', '9')), ".")),
						push(new DOMLiteralExpression(DOMLiteralType.Double, match())),
    			optionalGap()));
	}
	
	public Rule boolLiteral() {
		return Sequence(
				optionalGap(),
				Sequence(
						FirstOf("true", "false"),
						push(new DOMLiteralExpression(DOMLiteralType.Bool, match()))),
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