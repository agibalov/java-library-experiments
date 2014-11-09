package me.loki2302.messages

import org.junit.Test

import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory

import static org.junit.Assert.assertEquals

class MessageTemplateTest {
    @Test
    void canUseMessageTemplate() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()
        Validator validator = validatorFactory.getValidator()

        def person = new Person()
        person.age = 9

        def violations = validator.validate(person)
        assertEquals 1, violations.size()

        def violation = violations.first()
        assertEquals 'age', violation.propertyPath.toString()
        assertEquals 'Age should be between 10 and 1000. Your 9 doesn\'t qualify.', violation.message
        assertEquals 'Age should be between {min} and {max}. Your ${validatedValue} doesn\'t qualify.', violation.messageTemplate
    }

    static class Person {
        @org.hibernate.validator.constraints.Range(
                min = 10L, max = 1000L,
                message = 'Age should be between {min} and {max}. Your ${validatedValue} doesn\'t qualify.')
        int age
    }
}
