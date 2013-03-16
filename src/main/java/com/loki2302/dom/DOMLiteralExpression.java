package com.loki2302.dom;


public class DOMLiteralExpression implements DOMElement {
	private final DOMLiteralType literalType;
	private final String stringValue;
	
	public DOMLiteralExpression(DOMLiteralType literalType, String stringValue) {
		this.literalType = literalType;
		this.stringValue = stringValue;
	}
	
	public DOMLiteralType getLiteralType() {
		return literalType;
	}
	
	public String getStringValue() {
		return stringValue;
	}
}