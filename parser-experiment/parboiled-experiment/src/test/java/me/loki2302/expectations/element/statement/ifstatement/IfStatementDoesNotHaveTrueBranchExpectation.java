package me.loki2302.expectations.element.statement.ifstatement;

import static org.junit.Assert.assertNull;
import me.loki2302.dom.DOMIfStatement;
import me.loki2302.dom.DOMStatement;

public class IfStatementDoesNotHaveTrueBranchExpectation implements IfStatementExpectation {        
    @Override
    public void check(DOMIfStatement domIfStatement) {
        DOMStatement trueBranch = domIfStatement.getTrueBranch(); 
        assertNull(trueBranch);
    }
}