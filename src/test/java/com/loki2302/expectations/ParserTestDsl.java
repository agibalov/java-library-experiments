package com.loki2302.expectations;

import com.loki2302.dom.DOMBinaryExpressionType;
import com.loki2302.dom.DOMLiteralType;
import com.loki2302.dom.DOMUnaryExpressionType;
import com.loki2302.expectations.element.ElementExpectation;
import com.loki2302.expectations.element.ElementIsExpressionExpectation;
import com.loki2302.expectations.element.ElementIsStatementExpectation;
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
import com.loki2302.expectations.element.statement.StatementExpectation;
import com.loki2302.expectations.element.statement.StatementIsBreakStatementExpectation;
import com.loki2302.expectations.element.statement.StatementIsCompositeStatementExpectation;
import com.loki2302.expectations.element.statement.StatementIsContinueStatementExpectation;
import com.loki2302.expectations.element.statement.StatementIsDoWhileStatementExpectation;
import com.loki2302.expectations.element.statement.StatementIsExpressionStatementExpectation;
import com.loki2302.expectations.element.statement.StatementIsForStatementExpectation;
import com.loki2302.expectations.element.statement.StatementIsIfStatementExpectation;
import com.loki2302.expectations.element.statement.StatementIsNullStatementExpectation;
import com.loki2302.expectations.element.statement.StatementIsReturnStatementExpectation;
import com.loki2302.expectations.element.statement.StatementIsVariableDefinitionStatementExpectation;
import com.loki2302.expectations.element.statement.StatementIsWhileStatementExpectation;
import com.loki2302.expectations.element.statement.composite.CompositeStatementExpectation;
import com.loki2302.expectations.element.statement.composite.CompositeStatementHasChildExpectation;
import com.loki2302.expectations.element.statement.composite.CompositeStatementHasNumberOfChildrenExpectation;
import com.loki2302.expectations.element.statement.dowhile.DoWhileStatementExpectation;
import com.loki2302.expectations.element.statement.dowhile.DoWhileStatementHasBodyStatementExpectation;
import com.loki2302.expectations.element.statement.dowhile.DoWhileStatementHasConditionExpressionExpectation;
import com.loki2302.expectations.element.statement.expression.ExpressionStatementExpectation;
import com.loki2302.expectations.element.statement.expression.ExpressionStatementHasSpecificExpressionExpectation;
import com.loki2302.expectations.element.statement.forstatement.ForStatementExpectation;
import com.loki2302.expectations.element.statement.forstatement.ForStatementHasBodyStatementExpectation;
import com.loki2302.expectations.element.statement.forstatement.ForStatementHasConditionExpressionExpectation;
import com.loki2302.expectations.element.statement.forstatement.ForStatementHasInitializerStatementExpectation;
import com.loki2302.expectations.element.statement.forstatement.ForStatementHasNoConditionExpressionExpectation;
import com.loki2302.expectations.element.statement.forstatement.ForStatementHasStepStatementExpectation;
import com.loki2302.expectations.element.statement.ifstatement.IfStatementDoesNotHaveFalseBranchExpectation;
import com.loki2302.expectations.element.statement.ifstatement.IfStatementDoesNotHaveTrueBranchExpectation;
import com.loki2302.expectations.element.statement.ifstatement.IfStatementExpectation;
import com.loki2302.expectations.element.statement.ifstatement.IfStatementHasConditionExpressionExpectation;
import com.loki2302.expectations.element.statement.ifstatement.IfStatementHasFalseBranchExpectation;
import com.loki2302.expectations.element.statement.ifstatement.IfStatementHasTrueBranchExpectation;
import com.loki2302.expectations.element.statement.returnstatement.ReturnStatementExpectation;
import com.loki2302.expectations.element.statement.returnstatement.ReturnStatementHasExpressionExpectation;
import com.loki2302.expectations.element.statement.returnstatement.ReturnStatementHasNoExpressionExpectation;
import com.loki2302.expectations.element.statement.variabledefinition.VariableDefinitionHasExpressionExpectation;
import com.loki2302.expectations.element.statement.variabledefinition.VariableDefinitionHasTypeReferenceExpectation;
import com.loki2302.expectations.element.statement.variabledefinition.VariableDefinitionHasVariableNameExpectation;
import com.loki2302.expectations.element.statement.variabledefinition.VariableDefinitionStatementExpectation;
import com.loki2302.expectations.element.statement.whilestatement.WhileStatementExpectation;
import com.loki2302.expectations.element.statement.whilestatement.WhileStatementHasBodyStatementExpectation;
import com.loki2302.expectations.element.statement.whilestatement.WhileStatementHasConditionExpressionExpectation;
import com.loki2302.expectations.element.typereference.NamedTypeReferenceExpectation;
import com.loki2302.expectations.element.typereference.NamedTypeReferenceHasTypeNameExpectation;
import com.loki2302.expectations.element.typereference.TypeReferenceExpectation;
import com.loki2302.expectations.element.typereference.TypeReferenceIsNamedTypeExpectation;
import com.loki2302.expectations.parser.ParseResultExpectation;
import com.loki2302.expectations.parser.ParseResultIsBadExpectation;
import com.loki2302.expectations.parser.ParseResultIsOkExpectation;
import com.loki2302.parser.ExpressionParser;
import com.loki2302.parser.RootExpressionParser;
import com.loki2302.parser.PureStatementParser;
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
	
	public static StatementExpectation isCompositeStatement(CompositeStatementExpectation... expectations) {
        return new StatementIsCompositeStatementExpectation(expectations);
    }	
	
	public static StatementExpectation isReturn(ReturnStatementExpectation... expectations) {
        return new StatementIsReturnStatementExpectation(expectations);
    }
    
    public static ReturnStatementExpectation hasExpression(ExpressionExpectation... expectations) {
        return new ReturnStatementHasExpressionExpectation(expectations);
    }
    
    public static ReturnStatementExpectation hasNoExpression() {
        return new ReturnStatementHasNoExpressionExpectation();
    }
	
	public static StatementExpectation isContinue() {
        return new StatementIsContinueStatementExpectation();
    }
	
	public static StatementExpectation isBreak() {
        return new StatementIsBreakStatementExpectation();
    }
	
	public static CompositeStatementExpectation withChildren(int childrenCount) {
	    return new CompositeStatementHasNumberOfChildrenExpectation(childrenCount);
	}
	
	public static CompositeStatementExpectation withChild(int childIndex, StatementExpectation... expectations) {
	    return new CompositeStatementHasChildExpectation(childIndex, expectations);
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
	
	public static ExpressionParser parsePureStatement() {
        return new PureStatementParser();
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
    
    public static StatementExpectation ifStatement(IfStatementExpectation... expectations) {
        return new StatementIsIfStatementExpectation(expectations);
    }
    
    public static StatementExpectation isNullStatement() {
        return new StatementIsNullStatementExpectation();
    }
    
    public static IfStatementExpectation withCondition(ExpressionExpectation... expectations) {
        return new IfStatementHasConditionExpressionExpectation(expectations);
    }
    
    public static IfStatementExpectation hasTrueBranch(StatementExpectation... expectations) {
        return new IfStatementHasTrueBranchExpectation(expectations);
    }
    
    public static IfStatementExpectation hasNoTrueBranch() {
        return new IfStatementDoesNotHaveTrueBranchExpectation();
    }
    
    public static IfStatementExpectation hasFalseBranch(StatementExpectation... expectations) {
        return new IfStatementHasFalseBranchExpectation(expectations);
    }
    
    public static IfStatementExpectation hasNoFalseBranch() {
        return new IfStatementDoesNotHaveFalseBranchExpectation();
    }
    
    public static StatementExpectation isFor(ForStatementExpectation... expectations) {
        return new StatementIsForStatementExpectation(expectations);
    }
    
    public static StatementExpectation isWhileStatement(WhileStatementExpectation... expectations) {
        return new StatementIsWhileStatementExpectation(expectations);
    }
    
    public static StatementExpectation isDoWhileStatement(DoWhileStatementExpectation... expectations) {
        return new StatementIsDoWhileStatementExpectation(expectations);
    }
    
    public static ForStatementExpectation hasInitializerStatement(StatementExpectation... expectations) {        
        return new ForStatementHasInitializerStatementExpectation(expectations);
    }
        
    public static ForStatementExpectation forHasConditionExpression(ExpressionExpectation... expectations) {
        return new ForStatementHasConditionExpressionExpectation(expectations);
    }
    
    public static ForStatementExpectation forHasNoConditionExpression() {
        return new ForStatementHasNoConditionExpressionExpectation();
    }
    
    public static ForStatementExpectation hasStepStatement(StatementExpectation... expectations) {
        return new ForStatementHasStepStatementExpectation(expectations);
    }
        
    public static ForStatementExpectation forHasBody(StatementExpectation... expectations) {
        return new ForStatementHasBodyStatementExpectation(expectations);
    }
    
    public static WhileStatementExpectation whileHasConditionExpression(ExpressionExpectation... expectations) {
        return new WhileStatementHasConditionExpressionExpectation(expectations);
    }
    
    public static WhileStatementExpectation whileHasBody(StatementExpectation... expectations) {
        return new WhileStatementHasBodyStatementExpectation(expectations);
    }
    
    public static DoWhileStatementExpectation doWhileHasConditionExpression(ExpressionExpectation... expectations) {
        return new DoWhileStatementHasConditionExpressionExpectation(expectations);
    }
    
    public static DoWhileStatementExpectation doWhileHasBody(StatementExpectation... expectations) {
        return new DoWhileStatementHasBodyStatementExpectation(expectations);
    }
    
    public static StatementExpectation isVariableDefinition(VariableDefinitionStatementExpectation... expectations) {
        return new StatementIsVariableDefinitionStatementExpectation(expectations);
    }
    
    public static VariableDefinitionStatementExpectation hasTypeReference(TypeReferenceExpectation... expectations) {
        return new VariableDefinitionHasTypeReferenceExpectation(expectations);
    }
    
    public static VariableDefinitionStatementExpectation hasVariableName(String variableName) {
        return new VariableDefinitionHasVariableNameExpectation(variableName);
    } 
    
    public static VariableDefinitionStatementExpectation hasInitializerExpression(ExpressionExpectation... expectations) {
        return new VariableDefinitionHasExpressionExpectation(expectations);
    }
    
    public static TypeReferenceExpectation isNamedTypeReference(NamedTypeReferenceExpectation... expectations) {
        return new TypeReferenceIsNamedTypeExpectation(expectations);
    }
    
    public static NamedTypeReferenceExpectation withTypeName(String typeName) {
        return new NamedTypeReferenceHasTypeNameExpectation(typeName);
    }
    
    public static ExpressionExpectation isIntLiteralWithValueOf(String value) {
        return isLiteral(ofType(DOMLiteralType.Int), havingValueOf(value));
    }
}