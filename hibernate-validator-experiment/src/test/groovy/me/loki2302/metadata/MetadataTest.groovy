package me.loki2302.metadata

import org.hibernate.validator.constraints.NotEmpty
import org.junit.Test

import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory
import javax.validation.constraints.Max
import javax.validation.constraints.Min

import static org.junit.Assert.*

class MetadataTest {
    @Test
    void canReadConstraints() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()
        Validator validator = validatorFactory.getValidator()

        def beanDescriptor = validator.getConstraintsForClass(Person)
        assertTrue beanDescriptor.beanConstrained

        def constrainedProperties = beanDescriptor.constrainedProperties
        assertEquals 2, constrainedProperties.size()

        def nameConstraints = beanDescriptor.getConstraintsForProperty('name')
        assertNotNull nameConstraints
        assertEquals 1, nameConstraints.constraintDescriptors.size()
        assertEquals NotEmpty.class, nameConstraints.constraintDescriptors.first().annotation.annotationType()

        def ageConstraints = beanDescriptor.getConstraintsForProperty('age')
        assertNotNull ageConstraints
        assertEquals 2, ageConstraints.constraintDescriptors.size()
        assertTrue ageConstraints.constraintDescriptors.any { it.annotation.annotationType() == Min }
        assertTrue ageConstraints.constraintDescriptors.any { it.annotation.annotationType() == Max }
    }

    static class Person {
        @NotEmpty
        String name

        @Min(10L)
        @Max(1000L)
        int age
    }
}
