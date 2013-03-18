package com.loki2302.expectations.element.statement;

import static org.junit.Assert.assertEquals;

import com.loki2302.dom.DOMCompositeStatement;

public class CompositeStatementHasNumberOfChildrenExpectation implements CompositeStatementExpectation {
    private final int childCount;
    
    public CompositeStatementHasNumberOfChildrenExpectation(int childCount) {
        this.childCount = childCount;
    }
    
    @Override
    public void check(DOMCompositeStatement domCompositeStatement) {
        assertEquals(childCount, domCompositeStatement.getStatements().size());            
    }        
}