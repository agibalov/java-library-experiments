package me.loki2302.expectations;

import me.loki2302.dom.DOMBinaryExpressionType;
import me.loki2302.dom.DOMLiteralType;
import me.loki2302.dom.DOMUnaryExpressionType;
import me.loki2302.expectations.element.ElementExpectation;
import me.loki2302.expectations.element.ElementIsExpressionExpectation;
import me.loki2302.expectations.element.ElementIsFunctionDefinitionExpectation;
import me.loki2302.expectations.element.ElementIsProgramExpectation;
import me.loki2302.expectations.element.ElementIsStatementExpectation;
import me.loki2302.expectations.element.expression.ExpressionExpectation;
import me.loki2302.expectations.element.expression.ExpressionIsBinaryExpressionExpectation;
import me.loki2302.expectations.element.expression.ExpressionIsExplicitCastExpressionExpectation;
import me.loki2302.expectations.element.expression.ExpressionIsFunctionCallExpectation;
import me.loki2302.expectations.element.expression.ExpressionIsLiteralExpressionExpectation;
import me.loki2302.expectations.element.expression.ExpressionIsUnaryExpressionExpectation;
import me.loki2302.expectations.element.expression.ExpressionIsVariableReferenceExpectation;
import me.loki2302.expectations.element.expression.binary.BinaryExpressionExpectation;
import me.loki2302.expectations.element.expression.binary.BinaryExpressionHasSpecificLeftExpressionExpectation;
import me.loki2302.expectations.element.expression.binary.BinaryExpressionHasSpecificRightExpressionExpectation;
import me.loki2302.expectations.element.expression.binary.BinaryExpressionHasSpecificTypeExpectation;
import me.loki2302.expectations.element.expression.cast.ExplicitCastExpressionExpectation;
import me.loki2302.expectations.element.expression.cast.ExplicitCastExpressionHasInnerExpressionExpectation;
import me.loki2302.expectations.element.expression.cast.ExplicitCastExpressionHasTypeReferenceExpectation;
import me.loki2302.expectations.element.expression.functioncall.FunctionCallExpressionExpectation;
import me.loki2302.expectations.element.expression.functioncall.FunctionCallHasArgument;
import me.loki2302.expectations.element.expression.functioncall.FunctionCallHasArgumentsExpectation;
import me.loki2302.expectations.element.expression.functioncall.FunctionCallIsForSpecificFunctionNameExpectation;
import me.loki2302.expectations.element.expression.literal.LiteralExpressionExpectation;
import me.loki2302.expectations.element.expression.literal.LiteralExpressionHasSpecificTypeExpectation;
import me.loki2302.expectations.element.expression.literal.LiteralExpressionHasSpecificValueExpectation;
import me.loki2302.expectations.element.expression.unary.UnaryExpressionExpectation;
import me.loki2302.expectations.element.expression.unary.UnaryExpressionHasSpecificInnerExpressionExpectation;
import me.loki2302.expectations.element.expression.unary.UnaryExpressionHasSpecificTypeExpectation;
import me.loki2302.expectations.element.expression.variable.VariableHasSpecificNameExpectation;
import me.loki2302.expectations.element.expression.variable.VariableReferenceExpectation;
import me.loki2302.expectations.element.function.FunctionDefinitionExpectation;
import me.loki2302.expectations.element.function.FunctionDefinitionHasBodyExpectation;
import me.loki2302.expectations.element.function.FunctionDefinitionHasParameterExpectation;
import me.loki2302.expectations.element.function.FunctionDefinitionHasReturnTypeExpectation;
import me.loki2302.expectations.element.function.FunctionDefinitionHasSpecificNumberOfParametersExpectation;
import me.loki2302.expectations.element.function.FunctionDefinitionHasSpeicifcFunctioNameExpectation;
import me.loki2302.expectations.element.parameter.ParameterDefinitionExpectation;
import me.loki2302.expectations.element.parameter.ParameterDefinitionHasNameExpectation;
import me.loki2302.expectations.element.parameter.ParameterDefinitionHasTypeExpectation;
import me.loki2302.expectations.element.program.ProgramExpectation;
import me.loki2302.expectations.element.program.ProgramHasFunctionDefinitionExpectation;
import me.loki2302.expectations.element.program.ProgramHasSpecificNumberOfFunctionsExpectation;
import me.loki2302.expectations.element.statement.StatementExpectation;
import me.loki2302.expectations.element.statement.StatementIsBreakStatementExpectation;
import me.loki2302.expectations.element.statement.StatementIsCompositeStatementExpectation;
import me.loki2302.expectations.element.statement.StatementIsContinueStatementExpectation;
import me.loki2302.expectations.element.statement.StatementIsDoWhileStatementExpectation;
import me.loki2302.expectations.element.statement.StatementIsExpressionStatementExpectation;
import me.loki2302.expectations.element.statement.StatementIsForStatementExpectation;
import me.loki2302.expectations.element.statement.StatementIsIfStatementExpectation;
import me.loki2302.expectations.element.statement.StatementIsNullStatementExpectation;
import me.loki2302.expectations.element.statement.StatementIsReturnStatementExpectation;
import me.loki2302.expectations.element.statement.StatementIsVariableDefinitionStatementExpectation;
import me.loki2302.expectations.element.statement.StatementIsWhileStatementExpectation;
import me.loki2302.expectations.element.statement.composite.CompositeStatementExpectation;
import me.loki2302.expectations.element.statement.composite.CompositeStatementHasChildExpectation;
import me.loki2302.expectations.element.statement.composite.CompositeStatementHasNumberOfChildrenExpectation;
import me.loki2302.expectations.element.statement.dowhile.DoWhileStatementExpectation;
import me.loki2302.expectations.element.statement.dowhile.DoWhileStatementHasBodyStatementExpectation;
import me.loki2302.expectations.element.statement.dowhile.DoWhileStatementHasConditionExpressionExpectation;
import me.loki2302.expectations.element.statement.expression.ExpressionStatementExpectation;
import me.loki2302.expectations.element.statement.expression.ExpressionStatementHasSpecificExpressionExpectation;
import me.loki2302.expectations.element.statement.forstatement.ForStatementExpectation;
import me.loki2302.expectations.element.statement.forstatement.ForStatementHasBodyStatementExpectation;
import me.loki2302.expectations.element.statement.forstatement.ForStatementHasConditionExpressionExpectation;
import me.loki2302.expectations.element.statement.forstatement.ForStatementHasInitializerStatementExpectation;
import me.loki2302.expectations.element.statement.forstatement.ForStatementHasNoConditionExpressionExpectation;
import me.loki2302.expectations.element.statement.forstatement.ForStatementHasStepStatementExpectation;
import me.loki2302.expectations.element.statement.ifstatement.IfStatementDoesNotHaveFalseBranchExpectation;
import me.loki2302.expectations.element.statement.ifstatement.IfStatementDoesNotHaveTrueBranchExpectation;
import me.loki2302.expectations.element.statement.ifstatement.IfStatementExpectation;
import me.loki2302.expectations.element.statement.ifstatement.IfStatementHasConditionExpressionExpectation;
import me.loki2302.expectations.element.statement.ifstatement.IfStatementHasFalseBranchExpectation;
import me.loki2302.expectations.element.statement.ifstatement.IfStatementHasTrueBranchExpectation;
import me.loki2302.expectations.element.statement.returnstatement.ReturnStatementExpectation;
import me.loki2302.expectations.element.statement.returnstatement.ReturnStatementHasExpressionExpectation;
import me.loki2302.expectations.element.statement.returnstatement.ReturnStatementHasNoExpressionExpectation;
import me.loki2302.expectations.element.statement.variabledefinition.VariableDefinitionHasExpressionExpectation;
import me.loki2302.expectations.element.statement.variabledefinition.VariableDefinitionHasTypeReferenceExpectation;
import me.loki2302.expectations.element.statement.variabledefinition.VariableDefinitionHasVariableNameExpectation;
import me.loki2302.expectations.element.statement.variabledefinition.VariableDefinitionStatementExpectation;
import me.loki2302.expectations.element.statement.whilestatement.WhileStatementExpectation;
import me.loki2302.expectations.element.statement.whilestatement.WhileStatementHasBodyStatementExpectation;
import me.loki2302.expectations.element.statement.whilestatement.WhileStatementHasConditionExpressionExpectation;
import me.loki2302.expectations.element.typereference.NamedTypeReferenceExpectation;
import me.loki2302.expectations.element.typereference.NamedTypeReferenceHasTypeNameExpectation;
import me.loki2302.expectations.element.typereference.TypeReferenceExpectation;
import me.loki2302.expectations.element.typereference.TypeReferenceIsNamedTypeExpectation;
import me.loki2302.expectations.parser.ParseResultExpectation;
import me.loki2302.expectations.parser.ParseResultIsBadExpectation;
import me.loki2302.expectations.parser.ParseResultIsOkExpectation;
import me.loki2302.parser.ExpressionParser;
import me.loki2302.parser.FunctionDefinitionParser;
import me.loki2302.parser.ProgramParser;
import me.loki2302.parser.PureStatementParser;
import me.loki2302.parser.RootExpressionParser;
import me.loki2302.parser.StatementParser;

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
	
	public static ElementExpectation isFunction(FunctionDefinitionExpectation... expectations) {
        return new ElementIsFunctionDefinitionExpectation(expectations);
    }
    
    public static ElementExpectation isProgram(ProgramExpectation... expectations) {
        return new ElementIsProgramExpectation(expectations);
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
	
	public static FunctionCallExpressionExpectation withArgument(int argumentIndex, ExpressionExpectation... expectations) {
        return new FunctionCallHasArgument(argumentIndex, expectations);
    }
	
	public static FunctionCallExpressionExpectation withArguments(int argumentCount) {
        return new FunctionCallHasArgumentsExpectation(argumentCount);
    }
	
	public static FunctionCallExpressionExpectation withNoArguments() {
	    return new FunctionCallHasArgumentsExpectation(0);
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
	
	public static ExpressionParser parseFunction() {
        return new FunctionDefinitionParser();
    }
	
	public static ExpressionParser parseProgram() {
        return new ProgramParser();
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
    
    public static ExpressionExpectation isDoubleLiteralWithValueOf(String value) {
        return isLiteral(ofType(DOMLiteralType.Double), havingValueOf(value));
    }
    
    public static ExpressionExpectation IsExplicitCastExpression(ExplicitCastExpressionExpectation... expectations) {
        return new ExpressionIsExplicitCastExpressionExpectation(expectations);
    }
    
    public static ExplicitCastExpressionExpectation withTypeReference(TypeReferenceExpectation... expectations) {
        return new ExplicitCastExpressionHasTypeReferenceExpectation(expectations);
    }
    
    public static ExplicitCastExpressionExpectation explicitCastExpressionHasInnerExpression(ExpressionExpectation... expectations) {
        return new ExplicitCastExpressionHasInnerExpressionExpectation(expectations);
    }
    
    public static FunctionDefinitionExpectation hasReturnType(TypeReferenceExpectation... expectations) {
        return new FunctionDefinitionHasReturnTypeExpectation(expectations);
    }
    
    public static FunctionDefinitionExpectation functionHasName(String functionName) {
        return new FunctionDefinitionHasSpeicifcFunctioNameExpectation(functionName);
    }
    
    public static FunctionDefinitionExpectation functionHasNoParameters() {
        return hasParameters(0);
    }
    
    public static FunctionDefinitionExpectation functionHasParameter(int parameterIndex, ParameterDefinitionExpectation... expectations) {
        return new FunctionDefinitionHasParameterExpectation(parameterIndex, expectations);
    }
    
    public static ParameterDefinitionExpectation parameterHasType(TypeReferenceExpectation... expectations) {
        return new ParameterDefinitionHasTypeExpectation(expectations);
    }
    
    public static ParameterDefinitionExpectation parameterHasName(String parameterName) {
        return new ParameterDefinitionHasNameExpectation(parameterName);
    }
    
    public static FunctionDefinitionExpectation hasParameters(int parameterCount) {
        return new FunctionDefinitionHasSpecificNumberOfParametersExpectation(parameterCount);
    }
    
    public static FunctionDefinitionExpectation hasBody(StatementExpectation... expectations) {
        return new FunctionDefinitionHasBodyExpectation(expectations);
    }
    
    public static ProgramExpectation programHasFunctions(int functionCount) {
        return new ProgramHasSpecificNumberOfFunctionsExpectation(functionCount);
    }
    
    public static ProgramExpectation programHasFunction(int functionIndex, FunctionDefinitionExpectation... expectations) {
        return new ProgramHasFunctionDefinitionExpectation(functionIndex, expectations);
    }
}