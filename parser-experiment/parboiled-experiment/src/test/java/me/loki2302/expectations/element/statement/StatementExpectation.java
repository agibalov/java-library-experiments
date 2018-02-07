package me.loki2302.expectations.element.statement;

import me.loki2302.dom.DOMStatement;

public interface StatementExpectation {
    void check(DOMStatement domStatement);
}