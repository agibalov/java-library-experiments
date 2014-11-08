package me.loki2302
import org.hibernate.validator.constraints.NotEmpty
import org.junit.Before
import org.junit.Test

import javax.validation.ConstraintViolation
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory
import javax.validation.constraints.Max
import javax.validation.constraints.Min

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class BasicUseCaseTest {
    Validator validator;

    @Before
    void initValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void canGetMultipleValidationErrors() {
        Person person = new Person();
        person.name = "";
        person.age = -1;

        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertEquals 2, constraintViolations.size()
        assertTrue constraintViolations.any { it.propertyPath.toString() == 'name' }
        assertTrue constraintViolations.any { it.propertyPath.toString() == 'age' }
    }

    @Test
    void canGetNoErrorsWhenEverythingIsOk() {
        Person person = new Person();
        person.name = "loki2302";
        person.age = 40;

        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertTrue constraintViolations.empty
    }

    static class Person {
        @NotEmpty
        String name;

        @Min(0L)
        @Max(1000L)
        int age;
    }
}
