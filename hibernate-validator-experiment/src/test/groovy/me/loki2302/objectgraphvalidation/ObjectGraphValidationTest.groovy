package me.loki2302.objectgraphvalidation

import groovy.transform.builder.Builder
import org.hibernate.validator.constraints.NotEmpty
import org.junit.Before
import org.junit.Test

import javax.validation.Valid
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory
import javax.validation.constraints.NotNull

import static org.junit.Assert.*

class ObjectGraphValidationTest {
    Validator validator

    @Before
    void initValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()
        validator = validatorFactory.getValidator()
    }

    @Test
    void canGetTopLevelViolationsWhenObjectIsAbsolutelyEmpty() {
        def person = Person.builder().build()

        def violations = validator.validate(person)
        assertEquals 3, violations.size()
        assertTrue violations.any { it.propertyPath.toString() == 'name' }
        assertTrue violations.any { it.propertyPath.toString() == 'details' }
        assertTrue violations.any { it.propertyPath.toString() == 'interests' }
    }

    @Test
    void thereAreNoViolationsWhenAllTheDataIsThere() {
        def person = Person.builder()
                .name('loki2302')
                .details(PersonDetails.builder().about('smee').build())
                .interests([
                    Interest.builder().description('programming').build(),
                    Interest.builder().description('groovy').build()
                ])
                .build()

        def violations = validator.validate(person)
        assertTrue violations.empty
    }

    @Test
    void canDetectDetailsAboutIsEmpty() {
        def person = Person.builder()
                .name('loki2302')
                .details(PersonDetails.builder().about('').build())
                .interests([
                    Interest.builder().description('programming').build(),
                    Interest.builder().description('groovy').build()
                ])
                .build()

        def violations = validator.validate(person)
        assertEquals 1, violations.size()

        assertEquals 'details.about', violations.first().propertyPath.toString()
    }

    @Test
    void canDetectInterestsCollectionIsEmpty() {
        def person = Person.builder()
                .name('loki2302')
                .details(PersonDetails.builder().about('smee').build())
                .interests([
                ])
                .build()

        def violations = validator.validate(person)
        assertEquals 1, violations.size()

        assertEquals 'interests', violations.first().propertyPath.toString()
    }

    @Test
    void canDetectInterestDescriptionIsEmpty() {
        def person = Person.builder()
                .name('loki2302')
                .details(PersonDetails.builder().about('smee').build())
                .interests([
                    Interest.builder().description('programming').build(),
                    Interest.builder().description('').build()
                ])
                .build()

        def violations = validator.validate(person)
        assertEquals 1, violations.size()

        assertEquals 'interests[1].description', violations.first().propertyPath.toString()
    }

    @Builder
    static class Person {
        @NotEmpty
        String name

        @NotNull
        @Valid
        PersonDetails details

        @NotEmpty
        @Valid
        List<Interest> interests
    }

    @Builder
    static class PersonDetails {
        @NotEmpty
        String about
    }

    @Builder
    static class Interest {
        @NotEmpty
        String description
    }
}
