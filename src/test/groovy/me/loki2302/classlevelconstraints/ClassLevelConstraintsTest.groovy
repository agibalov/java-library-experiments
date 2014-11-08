package me.loki2302.classlevelconstraints
import org.junit.Before
import org.junit.Test

import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory

import static org.junit.Assert.*

class ClassLevelConstraintsTest {
    Validator validator

    @Before
    void initValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()
        validator = validatorFactory.getValidator()
    }

    @Test
    void canGetHandMadeConstraintViolations() {
        def person = new Person()
        person.likesProgramming = false
        person.knowsLanguages = 1

        def constraintViolations = validator.validate(person)
        assertEquals 2, constraintViolations.size()
        assertTrue constraintViolations.any { it.propertyPath.toString() == 'likesProgramming' }
        assertTrue constraintViolations.any { it.propertyPath.toString() == 'knowsLanguages' }
    }
}
