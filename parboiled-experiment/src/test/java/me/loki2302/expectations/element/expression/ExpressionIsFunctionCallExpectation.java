package me.loki2302.expectations.element.expression;

import static org.junit.Assert.assertTrue;
import me.loki2302.dom.DOMExpression;
import me.loki2302.dom.DOMFunctionCallExpression;
import me.loki2302.expectations.element.expression.functioncall.FunctionCallExpressionExpectation;

public class ExpressionIsFunctionCallExpectation implements ExpressionExpectation {
    private final FunctionCallExpressionExpectation[] expectations;
    
    public ExpressionIsFunctionCallExpectation() {
        this(new FunctionCallExpressionExpectation[]{});
    }
    
    public ExpressionIsFunctionCallExpectation(FunctionCallExpressionExpectation[] expectations) {
        this.expectations = expectations;
    }

    @Override
    public void check(DOMExpression domExpression) {
        assertTrue(domExpression instanceof DOMFunctionCallExpression);
        DOMFunctionCallExpression domFunctionCallExpression = (DOMFunctionCallExpression)domExpression;
        for(FunctionCallExpressionExpectation expectation : expectations) {
            expectation.check(domFunctionCallExpression);
        }            
    }
}