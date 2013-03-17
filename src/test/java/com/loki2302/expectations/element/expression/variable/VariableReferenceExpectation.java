package com.loki2302.expectations.element.expression.variable;

import com.loki2302.dom.DOMVariableReferenceExpression;

public interface VariableReferenceExpectation {
    void check(DOMVariableReferenceExpression domVariableReferenceExpression);
}