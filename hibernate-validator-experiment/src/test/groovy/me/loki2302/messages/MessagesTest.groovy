package me.loki2302.messages

import org.hibernate.validator.constraints.NotEmpty
import org.junit.Test

import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory

import static org.junit.Assert.assertEquals

class MessagesTest {
    @Test
    void canSpecifyMessageExplicitly() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()
        Validator validator = validatorFactory.getValidator()

        def person = new PersonWithHardcodedMessage()
        person.name = ''

        def violations = validator.validate(person)
        assertEquals 1, violations.size()

        def violation = violations.first()
        assertEquals 'name', violation.propertyPath.toString()
        assertEquals 'name should not be empty', violation.message
    }

    static class PersonWithHardcodedMessage {
        @NotEmpty(message = 'name should not be empty')
        String name
    }
}
