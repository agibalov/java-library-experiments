package me.loki2302.objectgraphvalidation

import groovy.transform.builder.Builder
import org.junit.Before
import org.junit.Test

import javax.validation.Valid
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory
import javax.validation.constraints.Min

import static org.junit.Assert.assertEquals

class GenericValidation {
    Validator validator

    @Before
    void initValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()
        validator = validatorFactory.getValidator()
    }

    @Test
    void canValidateSomethingWithPayload() {
        SomethingWithPayload<?> somethingWithPayload = SomethingWithPayload.builder()
                .payload(Idiot.builder().idiocyLevel(9).build()).build()

        def constraintViolations = validator.validate(somethingWithPayload)
        assertEquals(1, constraintViolations.size())
    }

    @Builder
    static class SomethingWithPayload<TPayload> {
        @Valid
        TPayload payload
    }

    static abstract class Person {
    }

    @Builder
    static class Idiot extends Person {
        @Min(10L)
        int idiocyLevel
    }
}
