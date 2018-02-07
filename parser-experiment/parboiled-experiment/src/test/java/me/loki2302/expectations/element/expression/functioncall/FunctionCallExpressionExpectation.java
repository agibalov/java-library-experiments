package me.loki2302.expectations.element.expression.functioncall;

import me.loki2302.dom.DOMFunctionCallExpression;

public interface FunctionCallExpressionExpectation {
    void check(DOMFunctionCallExpression domFunctionCallExpression);
}