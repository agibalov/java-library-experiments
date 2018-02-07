package me.loki2302.expectations.element.statement;

import static org.junit.Assert.assertTrue;
import me.loki2302.dom.DOMCompositeStatement;
import me.loki2302.dom.DOMStatement;
import me.loki2302.expectations.element.statement.composite.CompositeStatementExpectation;

public class StatementIsCompositeStatementExpectation implements StatementExpectation {
    private final CompositeStatementExpectation[] expectations;
    
    public StatementIsCompositeStatementExpectation() {
        this(new CompositeStatementExpectation[]{});
    }
    
    public StatementIsCompositeStatementExpectation(CompositeStatementExpectation[] expectations) {
        this.expectations = expectations;
    }
    
    @Override
    public void check(DOMStatement domStatement) {
        assertTrue(domStatement instanceof DOMCompositeStatement);
        DOMCompositeStatement domCompositeStatement = (DOMCompositeStatement)domStatement;
        for(CompositeStatementExpectation expectation : expectations) {
            expectation.check(domCompositeStatement);
        }
    }
}