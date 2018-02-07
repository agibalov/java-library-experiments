package me.loki2302
import org.junit.Test

import javax.validation.Validation
import javax.validation.constraints.Max

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

class MethodValidationTest {
    @Test
    void canValidateMethodArguments() {
        def validatorFactory = Validation.buildDefaultValidatorFactory()
        def executableValidator = validatorFactory.validator.forExecutables()

        def calculator = new Calculator()
        def violations = executableValidator.validateParameters(
                calculator,
                Calculator.getDeclaredMethod("addNumbers", int, int),
                [0, 11].toArray())

        assertEquals 2, violations.size()

        def aViolation = violations.find { it.propertyPath.toString() == 'addNumbers.arg0' }
        assertNotNull aViolation

        def bViolation = violations.find { it.propertyPath.toString() == 'addNumbers.arg1' }
        assertNotNull bViolation
    }

    @Test
    void canValidateMethodReturnValue() {
        def validatorFactory = Validation.buildDefaultValidatorFactory()
        def executableValidator = validatorFactory.validator.forExecutables()

        def calculator = new Calculator()
        def violations = executableValidator.validateReturnValue(
                calculator,
                Calculator.getDeclaredMethod("addNumbers", int, int),
                20)

        assertEquals 1, violations.size()

        def violation = violations.first()
        println violation
    }

    static class Calculator {
        // As of 5.1.3.Final, there's some sort of issue with return value validation:
        // https://hibernate.atlassian.net/browse/HV-847
        // https://hibernate.atlassian.net/browse/HV-726
        // I didn't manage to use @Range, because it's built via constraint composition
        // @Max works find

        @Max(10L)
        int addNumbers(
                @org.hibernate.validator.constraints.Range(min = 1L, max = 10L) int a,
                @org.hibernate.validator.constraints.Range(min = 1L, max = 10L) int b) {

            a + b
        }
    }
}
