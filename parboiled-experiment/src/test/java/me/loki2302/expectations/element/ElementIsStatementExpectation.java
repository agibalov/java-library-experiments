package me.loki2302.expectations.element;

import static org.junit.Assert.assertTrue;
import me.loki2302.dom.DOMElement;
import me.loki2302.dom.DOMStatement;
import me.loki2302.expectations.element.statement.StatementExpectation;

public class ElementIsStatementExpectation implements ElementExpectation {
    private final StatementExpectation[] expectations;
    
    public ElementIsStatementExpectation() {
        this(new StatementExpectation[]{});
    }
    
    public ElementIsStatementExpectation(StatementExpectation[] expectations) {
        this.expectations = expectations;
    }
    
    @Override
    public void check(DOMElement domElement) {
        assertTrue(domElement instanceof DOMStatement);
        DOMStatement domStatement = (DOMStatement)domElement;
        for(StatementExpectation expectation : expectations) {
            expectation.check(domStatement);
        }
    }    
}