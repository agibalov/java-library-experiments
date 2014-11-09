package me.loki2302.messages

import org.hibernate.validator.constraints.NotEmpty
import org.junit.Test

import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory

import static org.junit.Assert.assertEquals

class ValidationMessagesResourceTest {
    @Test
    void canUseValidationMessagesResource() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()
        Validator validator = validatorFactory.getValidator()

        def person = new PersonWithMessageTemplate()
        person.name = ''

        def violations = validator.validate(person)
        assertEquals 1, violations.size()

        def violation = violations.first()
        assertEquals 'name', violation.propertyPath.toString()
        assertEquals 'shouldn\'t be empty', violation.message
        println violation.messageTemplate
    }

    static class PersonWithMessageTemplate {
        // see ValidationMessages.properties in resources
        @NotEmpty(message = '{me.loki2302.NotEmpty.message}')
        String name
    }
}
