package com.loki2302;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.support.StringVar;
import com.loki2302.dom.DOMBinaryExpression;
import com.loki2302.dom.DOMBinaryExpressionType;
import com.loki2302.dom.DOMElement;
import com.loki2302.dom.DOMExpression;
import com.loki2302.dom.DOMLiteralExpression;
import com.loki2302.dom.DOMLiteralType;
import com.loki2302.dom.DOMUnaryExpression;
import com.loki2302.dom.DOMUnaryExpressionType;
import com.loki2302.dom.DOMVariableReferenceExpression;

// TODO: statement
// TODO: composite-statement
// TODO: expression-statement
// TODO: print-statement
// TODO: variable-definition-statement
// TODO: function-definition
// TODO: function-call-expression
// TODO: program
// TODO: variable-reference-expression
// TODO: if-else-statement
// TODO: for-statement
// TODO: while-statement
// TODO: do-while-statement
// TODO: continue-statement
// TODO: break-statement
// TODO: return-statement
// TODO: explicit-cast-expression
// +TODO: assignment-expression
// +TODO: unary-minus-expression
// +TODO: unary-plus-expression
// +TODO: unary-not-expression
// +TODO: prefix-increment-expression
// +TODO: postfix-increment-expression
// +TODO: prefix-decrement-expression
// +TODO: postfix-decrement-expression
// +TODO: increase-and-assign-expression
// +TODO: decrease-and-assign-expression
// +TODO: multiply-and-assign-expression
// +TODO: divide-and-assign-expression
// +TODO: less-expression
// +TODO: less-or-equal-expression
// +TODO: greater-expression
// +TODO: greater-or-equal-expression
// +TODO: equal-expression
// +TODO: not-equal-expression
// +TODO: and-expression
// +TODO: or-expression
// +TODO: int-literal-expression
// +TODO: double-literal-expression
// +TODO: bool-literal-expression
// +TODO: add-expression
// +TODO: sub-expression
// +TODO: mul-expression
// +TODO: div-expression
// +TODO: parentheses-expression

public class Grammar extends BaseParser<DOMElement> {    
    public Rule OPEN_PATENTHESIS = TERMINAL("(");
    public Rule CLOSE_PATENTHESIS = TERMINAL(")");
    
    public Rule expression() {
        return assignmentExpression();
    }
    
    public Rule parensExpression() {
        return Sequence(
                OPEN_PATENTHESIS,
                expression(),
                CLOSE_PATENTHESIS);
    }
    
    public Rule assignmentExpression() {
        StringVar op = new StringVar();
        return Sequence(
                orExpression(),
                ZeroOrMore(
                        decorateWithOptionalGaps(Sequence(
                                FirstOf("=", "+=", "-=", "*=", "/="),
                                op.set(match()))),                      
                        orExpression(),
                        push(Helper.domBinaryExpressionFromString(
                                op.get(), 
                                (DOMExpression)pop(1), 
                                (DOMExpression)pop()))));
    }
    
    public Rule orExpression() {
        StringVar op = new StringVar();
        return Sequence(
                andExpression(),
                ZeroOrMore(
                        decorateWithOptionalGaps(Sequence(
                                "||",
                                op.set(match()))),                      
                        andExpression(),
                        push(Helper.domBinaryExpressionFromString(
                                op.get(), 
                                (DOMExpression)pop(1), 
                                (DOMExpression)pop()))));
    }
    
    public Rule andExpression() {
        StringVar op = new StringVar();
        return Sequence(
                equalityComparisonExpression(),
                ZeroOrMore(
                        decorateWithOptionalGaps(Sequence(
                                "&&",
                                op.set(match()))),                      
                        equalityComparisonExpression(),
                        push(Helper.domBinaryExpressionFromString(
                                op.get(), 
                                (DOMExpression)pop(1), 
                                (DOMExpression)pop()))));
    }
    
    public Rule equalityComparisonExpression() {
        StringVar op = new StringVar();
        return Sequence(
                comparisonExpression(),
                ZeroOrMore(
                        decorateWithOptionalGaps(Sequence(
                                FirstOf("==", "!="),
                                op.set(match()))),                      
                        comparisonExpression(),
                        push(Helper.domBinaryExpressionFromString(
                                op.get(), 
                                (DOMExpression)pop(1), 
                                (DOMExpression)pop()))));
    }
    
    public Rule comparisonExpression() {
        StringVar op = new StringVar();
        return Sequence(
                addSubExpression(),
                ZeroOrMore(
                        decorateWithOptionalGaps(Sequence(
                                FirstOf("<=", "<", ">=", ">"),
                                op.set(match()))),                      
                        addSubExpression(),
                        push(Helper.domBinaryExpressionFromString(
                                op.get(), 
                                (DOMExpression)pop(1), 
                                (DOMExpression)pop()))));
    }
    
	public Rule addSubExpression() {
		StringVar op = new StringVar();
		return Sequence(
				mulDivExpression(),
				ZeroOrMore(
						decorateWithOptionalGaps(Sequence(
								FirstOf("+", "-"),
								op.set(match()))),						
						mulDivExpression(),
						push(Helper.domBinaryExpressionFromString(
								op.get(), 
								(DOMExpression)pop(1), 
								(DOMExpression)pop()))));
	}
	
	public Rule mulDivExpression() {
		StringVar op = new StringVar();
		return Sequence(
		        unaryExpression(),
				ZeroOrMore(
						decorateWithOptionalGaps(Sequence(
								FirstOf("*", "/"),
								op.set(match()))),
						unaryExpression(),
						push(Helper.domBinaryExpressionFromString(
								op.get(), 
								(DOMExpression)pop(1), 
								(DOMExpression)pop()))));
	}
	
	public Rule unaryExpression() {
	    return FirstOf(
	            prefixIncrementExpression(),
	            postfixIncrementExpression(),
	            prefixDecrementExpression(),
	            postfixDecrementExpression(),
	            plusSignExpression(),
	            minusSignExpression(),
	            notExpression(),
	            factor());
	}
	
	public Rule prefixIncrementExpression() {
	    return Sequence(
	            decorateWithOptionalGaps(String("++")), 
	            factor(),
	            push(new DOMUnaryExpression(DOMUnaryExpressionType.PrefixIncrement, (DOMExpression)pop())));
	}
	
	public Rule postfixIncrementExpression() {
	    return Sequence(                 
                factor(),
                decorateWithOptionalGaps(String("++")),
                push(new DOMUnaryExpression(DOMUnaryExpressionType.PostfixIncrement, (DOMExpression)pop())));
    }
	
	public Rule prefixDecrementExpression() {
	    return Sequence(
                decorateWithOptionalGaps(String("--")), 
                factor(),
                push(new DOMUnaryExpression(DOMUnaryExpressionType.PrefixDecrement, (DOMExpression)pop())));
    }
    
    public Rule postfixDecrementExpression() {
        return Sequence(                 
                factor(),
                decorateWithOptionalGaps(String("--")),
                push(new DOMUnaryExpression(DOMUnaryExpressionType.PostfixDecrement, (DOMExpression)pop())));
    }
    
    public Rule plusSignExpression() {
        return Sequence(
                decorateWithOptionalGaps(String("+")), 
                factor(),
                push(new DOMUnaryExpression(DOMUnaryExpressionType.PlusSign, (DOMExpression)pop())));
    }
    
    public Rule minusSignExpression() {
        return Sequence(
                decorateWithOptionalGaps(String("-")), 
                factor(),
                push(new DOMUnaryExpression(DOMUnaryExpressionType.MinusSign, (DOMExpression)pop())));
    }
    
    public Rule notExpression() {
        return Sequence(
                decorateWithOptionalGaps(String("!")), 
                factor(),
                push(new DOMUnaryExpression(DOMUnaryExpressionType.Not, (DOMExpression)pop())));
    }
	
	public Rule factor() {
	    return FirstOf(
	            parensExpression(),
	            literal(),
	            variableReference());
	}
	
	public Rule variableReference() {
	    return decorateWithOptionalGaps(
	            Sequence(
	                    name(),
	                    push(new DOMVariableReferenceExpression(match()))));
	}
	
	public Rule name() {
	    return OneOrMore(
	            FirstOf(
	                    CharRange('a', 'z'),
	                    CharRange('A', 'Z')));
	}
	
	public Rule literal() {
		return FirstOf(
				boolLiteral(),
				doubleLiteral(),
				intLiteral());
	}
	
	public Rule intLiteral() {
		return decorateWithOptionalGaps(
				Sequence(
						OneOrMore(CharRange('0', '9')),
						push(new DOMLiteralExpression(DOMLiteralType.Int, match()))));
	}
	
	public Rule doubleLiteral() {
		return decorateWithOptionalGaps(
				Sequence(
						FirstOf(
								Sequence(".", OneOrMore(CharRange('0', '9'))),
								Sequence(OneOrMore(CharRange('0', '9')), ".", OneOrMore(CharRange('0', '9'))),
								Sequence(OneOrMore(CharRange('0', '9')), ".")),
						push(new DOMLiteralExpression(DOMLiteralType.Double, match()))));
	}
	
	public Rule boolLiteral() {
		return decorateWithOptionalGaps(
				Sequence(
						FirstOf("true", "false"),
						push(new DOMLiteralExpression(DOMLiteralType.Bool, match()))));
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
	
	public Rule decorateWithOptionalGaps(Rule rule) {
		return Sequence(optionalGap(), rule, optionalGap());
	}
	
	public Rule TERMINAL(String s) {
	    return decorateWithOptionalGaps(String(s));
	}
	
	private static class Helper {
		public static DOMBinaryExpression domBinaryExpressionFromString(
				String operation, 
				DOMExpression leftExpression, 
				DOMExpression rightExpression) {
			
			DOMBinaryExpressionType expressionType = null;
			
			if(operation.equals("+")) {
				expressionType = DOMBinaryExpressionType.Add;
			} else if(operation.equals("-")) {
				expressionType = DOMBinaryExpressionType.Sub;
			} else if(operation.equals("*")) {
				expressionType = DOMBinaryExpressionType.Mul;
			} else if(operation.equals("/")) {
				expressionType = DOMBinaryExpressionType.Div;
			} else if(operation.equals("<")) {
			    expressionType = DOMBinaryExpressionType.Less;
			} else if(operation.equals("<=")) {
			    expressionType = DOMBinaryExpressionType.LessOrEqual;
			} else if(operation.equals(">")) {
			    expressionType = DOMBinaryExpressionType.Greater;
			} else if(operation.equals(">=")) {
			    expressionType = DOMBinaryExpressionType.GreaterOrEqual;
			} else if(operation.equals("!=")) {
			    expressionType = DOMBinaryExpressionType.NotEqual;
			} else if(operation.equals("==")) {
			    expressionType = DOMBinaryExpressionType.Equal;
			} else if(operation.equals("&&")) {
                expressionType = DOMBinaryExpressionType.And;
			} else if(operation.equals("||")) {
                expressionType = DOMBinaryExpressionType.Or;
			} else if(operation.equals("=")) {
			    expressionType = DOMBinaryExpressionType.Assignment;
			} else if(operation.equals("+=")) {
                expressionType = DOMBinaryExpressionType.AddAndAssign;
			} else if(operation.equals("-=")) {
                expressionType = DOMBinaryExpressionType.SubAndAssign;
			} else if(operation.equals("*=")) {
                expressionType = DOMBinaryExpressionType.MulAndAssign;
			} else if(operation.equals("/=")) {
                expressionType = DOMBinaryExpressionType.DivAndAssign;
			}
                
			if(expressionType == null) {
				throw new RuntimeException(String.format("Unknown operation - %s", operation));
			}
			
			return new DOMBinaryExpression(expressionType, leftExpression, rightExpression);
		}
	}
}