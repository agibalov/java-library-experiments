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

public class Grammar extends BaseParser<DOMElement> {    
    public Rule OPEN_PATENTHESIS = TERMINAL("(");
    public Rule CLOSE_PATENTHESIS = TERMINAL(")");
    
    public Rule expression() {
        return addSubExpression();
    }
    
    public Rule parensExpression() {
        return Sequence(
                OPEN_PATENTHESIS,
                expression(),
                CLOSE_PATENTHESIS);
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
		        factor(),
				ZeroOrMore(
						decorateWithOptionalGaps(Sequence(
								FirstOf("*", "/"),
								op.set(match()))),
						factor(),
						push(Helper.domBinaryExpressionFromString(
								op.get(), 
								(DOMExpression)pop(1), 
								(DOMExpression)pop()))));
	}
	
	public Rule factor() {
	    return FirstOf(
	            parensExpression(),
	            literal());
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
			}			
			
			if(expressionType == null) {
				throw new RuntimeException(String.format("Unknown operation - %s", operation));
			}
			
			return new DOMBinaryExpression(expressionType, leftExpression, rightExpression);
		}
	}
}