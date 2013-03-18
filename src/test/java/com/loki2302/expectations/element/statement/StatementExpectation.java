package com.loki2302.expectations.element.statement;

import com.loki2302.dom.DOMStatement;

public interface StatementExpectation {
    void check(DOMStatement domStatement);
}