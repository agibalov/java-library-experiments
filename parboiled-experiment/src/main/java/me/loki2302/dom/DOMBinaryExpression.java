package me.loki2302.dom;

public class DOMBinaryExpression implements DOMExpression {
	private final DOMBinaryExpressionType expressionType;
	private final DOMExpression leftExpression;
	private final DOMExpression rightExpression;
	
	public DOMBinaryExpression(
			DOMBinaryExpressionType expressionType,
			DOMExpression leftExpression, 
			DOMExpression rightExpression) {
		
		this.expressionType = expressionType;
		this.leftExpression = leftExpression;
		this.rightExpression = rightExpression;
	}
	
	public DOMBinaryExpressionType getExpressionType() {
		return expressionType;
	}
	
	public DOMExpression getLeftExpression() {
		return leftExpression;
	}
	
	public DOMExpression getRightExpression() {
		return rightExpression;
	}
}