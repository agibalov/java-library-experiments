package com.loki2302.expectations.element.statement;

import static org.junit.Assert.assertTrue;

import java.util.List;

import com.loki2302.dom.DOMCompositeStatement;
import com.loki2302.dom.DOMStatement;

public class CompositeStatementHasChildExpectation implements CompositeStatementExpectation {
    private final int childIndex;
    private final StatementExpectation[] expectations;
    
    public CompositeStatementHasChildExpectation(
            int childIndex, 
            StatementExpectation[] expectations) {
        
        this.childIndex = childIndex;
        this.expectations = expectations;
    }

    @Override
    public void check(DOMCompositeStatement domCompositeStatement) {
        List<DOMStatement> childStatements = domCompositeStatement.getStatements();
        assertTrue(childStatements.size() > childIndex);
        DOMStatement domStatement = childStatements.get(childIndex);
        for(StatementExpectation expectation : expectations) {
            expectation.check(domStatement);
        }
    }
}