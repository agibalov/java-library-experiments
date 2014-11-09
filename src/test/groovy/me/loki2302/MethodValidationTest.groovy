package me.loki2302

import org.junit.Test

import javax.validation.Validation
import javax.validation.ValidatorFactory

import static org.junit.Assert.*

class MethodValidationTest {
    @Test
    void canUseMethodArgumentsValidation() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()
        def executableValidator = validatorFactory.validator.forExecutables()

        def calculator = new Calculator()
        def violations = executableValidator.validateParameters(
                calculator,
                Calculator.getDeclaredMethod("addNumbers", int, int),
                [0, 11].toArray())

        assertEquals 2, violations.size()

        println violations

        def aViolation = violations.find { it.propertyPath.toString() == 'addNumbers.arg0' }
        assertNotNull aViolation

        def bViolation = violations.find { it.propertyPath.toString() == 'addNumbers.arg1' }
        assertNotNull bViolation
    }

    static class Calculator {
        int addNumbers(
                @org.hibernate.validator.constraints.Range(min = 1L, max = 10L) int a,
                @org.hibernate.validator.constraints.Range(min = 1L, max = 10L) int b) {

            a + b
        }
    }
}
