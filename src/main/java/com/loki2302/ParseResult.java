package com.loki2302;

import com.loki2302.dom.DOMElement;

public class ParseResult {
	private boolean isOk;
	private DOMElement domElement;
	
	public boolean isOk() {
		return isOk;
	}
	
	public DOMElement getDOMElement() {
		return domElement;
	}
	
	public static ParseResult ok(DOMElement domElement) {
		ParseResult parseResult = new ParseResult();
		parseResult.isOk = true;
		parseResult.domElement = domElement;
		return parseResult;
	}
	
	public static ParseResult fail() {
		ParseResult parseResult = new ParseResult();
		parseResult.isOk = false;
		return parseResult;
	}
}