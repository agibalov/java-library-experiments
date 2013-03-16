package com.loki2302.expectations;

import static org.junit.Assert.assertTrue;

import com.loki2302.ParseResult;
import com.loki2302.dom.DOMElement;


public class ParseResultIsOkExpectation implements ParseResultExpectation {
	private final DOMElementExpectation domElementDescriptor;
	
	public ParseResultIsOkExpectation(DOMElementExpectation domElementDescriptor) {
		this.domElementDescriptor = domElementDescriptor;
	}
	
	@Override
	public void check(ParseResult parseResult) {
		assertTrue(parseResult.isOk());			
		DOMElement domElement = parseResult.getDOMElement();
		domElementDescriptor.check(domElement);
	}
}