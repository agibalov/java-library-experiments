package me.loki2302.expectations.parser;

import static org.junit.Assert.assertTrue;
import me.loki2302.ParseResult;
import me.loki2302.dom.DOMElement;
import me.loki2302.expectations.element.ElementExpectation;

public class ParseResultIsOkExpectation implements ParseResultExpectation {
	private final ElementExpectation domElementDescriptor;
	
	public ParseResultIsOkExpectation(ElementExpectation domElementDescriptor) {
		this.domElementDescriptor = domElementDescriptor;
	}
	
	@Override
	public void check(ParseResult parseResult) {
		assertTrue(parseResult.isOk());			
		DOMElement domElement = parseResult.getDOMElement();
		domElementDescriptor.check(domElement);
	}
}