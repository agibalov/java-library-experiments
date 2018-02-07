package me.loki2302.messages

import org.hibernate.validator.constraints.NotEmpty
import org.junit.Test

import javax.validation.MessageInterpolator
import javax.validation.Validation

import static org.junit.Assert.*

class CustomMessageInterpolatorTest {
    @Test
    void dummy() {
        def validatorConfiguration = Validation.byDefaultProvider().configure();
        def interpolator = new CustomMessageInterpolator(validatorConfiguration.defaultMessageInterpolator)
        validatorConfiguration = validatorConfiguration.messageInterpolator(interpolator)
        def validatorFactory = validatorConfiguration.buildValidatorFactory()
        def validator = validatorFactory.validator

        def person = new Person()

        interpolator.locale = Locale.ENGLISH
        assertEquals 'shouldn\'t be empty', validator.validate(person).first().message

        interpolator.locale = Locale.JAPAN
        assertEquals 'pustoe nelzya', validator.validate(person).first().message
    }

    static class Person {
        // see ValidationMessages.properties in resources
        @NotEmpty(message = '{me.loki2302.NotEmpty.message}')
        String name
    }

    static class CustomMessageInterpolator implements MessageInterpolator {
        private final MessageInterpolator delegate
        Locale locale

        CustomMessageInterpolator(MessageInterpolator delegate) {
            this.delegate = delegate
        }

        @Override
        String interpolate(String messageTemplate, MessageInterpolator.Context context) {
            return interpolate(messageTemplate, context, locale)
        }

        @Override
        String interpolate(String messageTemplate, MessageInterpolator.Context context, Locale locale) {
            return delegate.interpolate(messageTemplate, context, locale)
        }
    }
}
