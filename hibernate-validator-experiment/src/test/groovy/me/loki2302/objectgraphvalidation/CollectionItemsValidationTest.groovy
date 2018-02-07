package me.loki2302.objectgraphvalidation

import groovy.transform.builder.Builder
import org.junit.Before
import org.junit.Test

import javax.validation.Valid
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory
import javax.validation.constraints.Size

import static org.junit.Assert.*

class CollectionItemsValidationTest {
    Validator validator

    @Before
    void initValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()
        validator = validatorFactory.getValidator()
    }

    @Test
    void canValidateItemsWithinACollection() {
        def people = People.builder().items([
                Person.builder().name('loki2302').build(),
                Person.builder().name('john').build(),
        ]).build()

        def constraintViolations = validator.validate(people)
        assertEquals(1, constraintViolations.size())
    }

    @Builder
    static class People {
        @Valid
        List<Person> items
    }

    @Builder
    static class Person {
        @Size(min = 5)
        String name
    }
}
