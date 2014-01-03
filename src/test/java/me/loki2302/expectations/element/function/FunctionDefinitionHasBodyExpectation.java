package me.loki2302.expectations.element.function;

import static org.junit.Assert.assertNotNull;
import me.loki2302.dom.DOMFunctionDefinition;
import me.loki2302.dom.DOMStatement;
import me.loki2302.expectations.element.statement.StatementExpectation;

public class FunctionDefinitionHasBodyExpectation implements FunctionDefinitionExpectation {
    private final StatementExpectation[] expectations;
    
    public FunctionDefinitionHasBodyExpectation(StatementExpectation[] expectations) {
        this.expectations = expectations;
    }

    @Override
    public void check(DOMFunctionDefinition domFunctionDefinition) {
        DOMStatement domStatement = domFunctionDefinition.getBody();
        assertNotNull(domStatement);
        for(StatementExpectation expectation : expectations) {
            expectation.check(domStatement);
        }
    }
}