package me.loki2302.constraintvalidatorinjection

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import org.junit.Test

import javax.validation.*

import static org.junit.Assert.*

class ConstraintValidatorInjectionTest {
    @Test
    void canInjectIntoConstraintValidator() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(MyValidationService).asEagerSingleton()
            }
        })

        def myConstraintValidatorFactory = new MyConstraintValidatorFactory(injector)

        ValidatorFactory validatorFactory = Validation.byDefaultProvider()
                .configure()
                .constraintValidatorFactory(myConstraintValidatorFactory)
                .buildValidatorFactory()
        Validator validator = validatorFactory.validator

        Person person = new Person()
        person.name = 'Andrey'
        def constraintViolations = validator.validate(person)
        assertEquals 1, constraintViolations.size()
    }

    static class Person {
        @GoodName
        String name
    }

    static class MyConstraintValidatorFactory implements ConstraintValidatorFactory {
        private final Injector injector

        public MyConstraintValidatorFactory(Injector injector) {
            this.injector = injector
        }

        @Override
        def <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
            return injector.getInstance(key)
        }

        @Override
        void releaseInstance(ConstraintValidator<?, ?> instance) {
        }
    }
}
