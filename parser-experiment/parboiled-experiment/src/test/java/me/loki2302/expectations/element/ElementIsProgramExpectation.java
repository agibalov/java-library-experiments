package me.loki2302.expectations.element;

import static org.junit.Assert.assertTrue;
import me.loki2302.dom.DOMElement;
import me.loki2302.dom.DOMProgram;
import me.loki2302.expectations.element.program.ProgramExpectation;

public class ElementIsProgramExpectation implements ElementExpectation {
    private final ProgramExpectation[] expectations;
    
    public ElementIsProgramExpectation(ProgramExpectation[] expectations) {
        this.expectations = expectations;
    }
    
    @Override
    public void check(DOMElement domElement) {
        assertTrue(domElement instanceof DOMProgram);
        DOMProgram domProgram = (DOMProgram)domElement;
        for(ProgramExpectation expectation : expectations) {
            expectation.check(domProgram);
        }
    }        
}