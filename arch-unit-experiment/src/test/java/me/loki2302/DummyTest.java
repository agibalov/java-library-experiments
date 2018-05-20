package me.loki2302;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.EvaluationResult;
import com.tngtech.archunit.lang.FailureReport;
import org.junit.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DummyTest {
    private final static ArchRule rule = noClasses().should()
            .haveSimpleName("Asshole")
            .because("it would be rude");

    @Test
    public void canGetAConcern() {
        JavaClasses classes = new ClassFileImporter()
                .importPackages("me.loki2302.badpackage");
        EvaluationResult evaluationResult = rule.evaluate(classes);
        FailureReport failureReport = evaluationResult.getFailureReport();
        assertFalse(failureReport.isEmpty());
        assertEquals("class me.loki2302.badpackage.Asshole has simple name 'Asshole'",
                failureReport.getDetails().get(0));
    }

    @Test
    public void canVerifyEverythingIsOk() {
        JavaClasses classes = new ClassFileImporter()
                .importPackages("me.loki2302.goodpackage");
        EvaluationResult evaluationResult = rule.evaluate(classes);
        FailureReport failureReport = evaluationResult.getFailureReport();
        assertTrue(failureReport.isEmpty());
    }
}
