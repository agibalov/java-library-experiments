package com.loki2302.expectations;

import com.loki2302.dom.DOMBinaryExpressionType;
import com.loki2302.dom.DOMLiteralType;
import com.loki2302.dom.DOMUnaryExpressionType;
import com.loki2302.expectations.element.ElementExpectation;
import com.loki2302.expectations.element.ElementIsExpressionExpectation;
import com.loki2302.expectations.element.expression.ExpressionExpectation;
import com.loki2302.expectations.element.expression.ExpressionIsBinaryExpressionExpectation;
import com.loki2302.expectations.element.expression.ExpressionIsFunctionCallExpectation;
import com.loki2302.expectations.element.expression.ExpressionIsLiteralExpressionExpectation;
import com.loki2302.expectations.element.expression.ExpressionIsUnaryExpressionExpectation;
import com.loki2302.expectations.element.expression.ExpressionIsVariableReferenceExpectation;
import com.loki2302.expectations.element.expression.binary.BinaryExpressionExpectation;
import com.loki2302.expectations.element.expression.binary.BinaryExpressionHasSpecificLeftExpressionExpectation;
import com.loki2302.expectations.element.expression.binary.BinaryExpressionHasSpecificRightExpressionExpectation;
import com.loki2302.expectations.element.expression.binary.BinaryExpressionHasSpecificTypeExpectation;
import com.loki2302.expectations.element.expression.functioncall.FunctionCallExpressionExpectation;
import com.loki2302.expectations.element.expression.functioncall.FunctionCallHasNParametersExpectation;
import com.loki2302.expectations.element.expression.functioncall.FunctionCallHasParameterN;
import com.loki2302.expectations.element.expression.functioncall.FunctionCallIsForSpecificFunctionNameExpectation;
import com.loki2302.expectations.element.expression.literal.LiteralExpressionExpectation;
import com.loki2302.expectations.element.expression.literal.LiteralExpressionHasSpecificTypeExpectation;
import com.loki2302.expectations.element.expression.literal.LiteralExpressionHasSpecificValueExpectation;
import com.loki2302.expectations.element.expression.unary.UnaryExpressionExpectation;
import com.loki2302.expectations.element.expression.unary.UnaryExpressionHasSpecificInnerExpressionExpectation;
import com.loki2302.expectations.element.expression.unary.UnaryExpressionHasSpecificTypeExpectation;
import com.loki2302.expectations.element.expression.variable.VariableHasSpecificNameExpectation;
import com.loki2302.expectations.element.expression.variable.VariableReferenceExpectation;
import com.loki2302.expectations.element.statement.ElementIsStatementExpectation;
import com.loki2302.expectations.element.statement.ExpressionStatementExpectation;
import com.loki2302.expectations.element.statement.ExpressionStatementHasSpecificExpressionExpectation;
import com.loki2302.expectations.element.statement.StatementExpectation;
import com.loki2302.expectations.element.statement.StatementIsExpressionStatementExpectation;
import com.loki2302.expectations.parser.ParseResultExpectation;
import com.loki2302.expectations.parser.ParseResultIsBadExpectation;
import com.loki2302.expectations.parser.ParseResultIsOkExpectation;
import com.loki2302.parser.ExpressionParser;
import com.loki2302.parser.RootExpressionParser;
import com.loki2302.parser.StatementParser;

public class ParserTestDsl {
	private ParserTestDsl() {		
	}
	
	public static ParseResultExpectation result(ElementExpectation domElementDescriptor) {
		return new ParseResultIsOkExpectation(domElementDescriptor);
	}	
	
	public static ParseResultExpectation fail() {
		return new ParseResultIsBadExpectation();
	}
	
	public static ElementExpectation isExpression(ExpressionExpectation... expectations) {
		return new ElementIsExpressionExpectation(expectations);
	}
	
	public static ElementExpectation isStatement(StatementExpectation... expectations) {
	    return new ElementIsStatementExpectation(expectations);
	}
	
	public static StatementExpectation isExpressionStatement(ExpressionStatementExpectation... expectations) {
	    return new StatementIsExpressionStatementExpectation(expectations);
	}
	
	public static ExpressionStatementExpectation withExpression(ExpressionExpectation... expectations) {
	    return new ExpressionStatementHasSpecificExpressionExpectation(expectations);
	}
	
	public static ExpressionExpectation isLiteral(LiteralExpressionExpectation... expectations) {
		return new ExpressionIsLiteralExpressionExpectation(expectations);
	}
	
	public static LiteralExpressionExpectation ofType(DOMLiteralType literalType) {
		return new LiteralExpressionHasSpecificTypeExpectation(literalType);
	}
	
	public static LiteralExpressionExpectation havingValueOf(String stringValue) {
		return new LiteralExpressionHasSpecificValueExpectation(stringValue);
	}
	
	public static ExpressionExpectation isBinary(BinaryExpressionExpectation... expectations) {
		return new ExpressionIsBinaryExpressionExpectation(expectations);
	}
	
	public static ExpressionExpectation isUnary(UnaryExpressionExpectation... expectations) {
	    return new ExpressionIsUnaryExpressionExpectation(expectations);
	}
	
	public static ExpressionExpectation isVariable(VariableReferenceExpectation... expectations) {
	    return new ExpressionIsVariableReferenceExpectation(expectations);
	}
	
	public static ExpressionExpectation isFunctionCall(FunctionCallExpressionExpectation... expectations) {
        return new ExpressionIsFunctionCallExpectation(expectations);
    }
	
	public static VariableReferenceExpectation withName(String name) {
	    return new VariableHasSpecificNameExpectation(name);
	}
	
	public static FunctionCallExpressionExpectation named(String name) {
        return new FunctionCallIsForSpecificFunctionNameExpectation(name);
    }
	
	public static FunctionCallExpressionExpectation withParameter(int parameterIndex, ExpressionExpectation... expectations) {
        return new FunctionCallHasParameterN(parameterIndex, expectations);
    }
	
	public static FunctionCallExpressionExpectation withParameters(int parameterCount) {
        return new FunctionCallHasNParametersExpectation(parameterCount);
    }
	
	public static FunctionCallExpressionExpectation withNoParameters() {
	    return new FunctionCallHasNParametersExpectation(0);
	}
	
	public static UnaryExpressionExpectation ofType(DOMUnaryExpressionType expressionType) {
	    return new UnaryExpressionHasSpecificTypeExpectation(expressionType);
	}
	
	public static BinaryExpressionExpectation ofType(DOMBinaryExpressionType expressionType) {
		return new BinaryExpressionHasSpecificTypeExpectation(expressionType);
	}
	
	public static ExpressionParser parseExpression() {
	    return new RootExpressionParser();
	}
	
	public static ExpressionParser parseStatement() {
        return new StatementParser();
    }
	
	public static BinaryExpressionExpectation withLeftExpression(ExpressionExpectation... leftExpressionExpectations) {
		return new BinaryExpressionHasSpecificLeftExpressionExpectation(leftExpressionExpectations);
	}
	
	public static BinaryExpressionExpectation withRightExpression(ExpressionExpectation... rightExpressionExpectations) {
		return new BinaryExpressionHasSpecificRightExpressionExpectation(rightExpressionExpectations);
	}	
	
	public static UnaryExpressionExpectation withInnerExpression(ExpressionExpectation... expectations) {
	    return new UnaryExpressionHasSpecificInnerExpressionExpectation(expectations);
	}

    public static BinaryExpressionExpectation withIntLiteralAsLeftExpression(String stringValue) {
        return withLeftExpression(isLiteral(ofType(DOMLiteralType.Int), havingValueOf(stringValue)));
    }
    
    public static BinaryExpressionExpectation withIntLiteralAsRightExpression(String stringValue) {
        return withRightExpression(isLiteral(ofType(DOMLiteralType.Int), havingValueOf(stringValue)));
    }
    
    public static UnaryExpressionExpectation withIntLiteralAsInnerExpression(String stringValue) {
        return withInnerExpression(isLiteral(ofType(DOMLiteralType.Int), havingValueOf(stringValue)));
    }
}