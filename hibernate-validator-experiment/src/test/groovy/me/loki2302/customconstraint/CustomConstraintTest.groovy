package me.loki2302.customconstraint
import org.junit.Before
import org.junit.Test

import javax.validation.ConstraintViolation
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory

import static org.junit.Assert.*

class CustomConstraintTest {
    Validator validator

    @Before
    void initValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()
        validator = validatorFactory.getValidator()
    }

    @Test
    void canUseCustomConstraint() {
        Person person = new Person()
        person.name = 'Andrey'

        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person)
        assertEquals 1, constraintViolations.size()
        assertTrue constraintViolations.any { it.propertyPath.toString() == 'name' }
    }

    static class Person {
        @GoodName(shouldContain = 'loki')
        String name
    }
}
