package me.loki2302.objectgraphvalidation

import groovy.transform.builder.Builder
import org.junit.Before
import org.junit.Test

import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory
import javax.validation.constraints.Min

import static org.junit.Assert.*

class InheritanceValidation {
    Validator validator

    @Before
    void initValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()
        validator = validatorFactory.getValidator()
    }

    @Test
    void canValidateAnIdiot() {
        Person person = Idiot.builder().idiocyLevel(9).build()
        def constraintViolations = validator.validate(person)
        assertEquals(1, constraintViolations.size())
    }

    static abstract class Person {
    }

    @Builder
    static class Idiot extends Person {
        @Min(10L)
        int idiocyLevel
    }
}
