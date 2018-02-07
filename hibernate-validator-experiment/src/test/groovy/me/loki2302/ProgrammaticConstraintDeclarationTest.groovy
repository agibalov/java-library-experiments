package me.loki2302

import org.hibernate.validator.HibernateValidator
import org.hibernate.validator.HibernateValidatorConfiguration
import org.hibernate.validator.cfg.ConstraintMapping
import org.hibernate.validator.cfg.defs.NotEmptyDef
import org.hibernate.validator.cfg.defs.RangeDef
import org.junit.Test

import javax.validation.Validation
import javax.validation.Validator
import java.lang.annotation.ElementType

import static org.junit.Assert.*

class ProgrammaticConstraintDeclarationTest {
    @Test
    void dummy() {
        HibernateValidatorConfiguration configuration = Validation.byProvider(HibernateValidator)
                .configure()

        ConstraintMapping constraintMapping = configuration.createConstraintMapping()

        constraintMapping
                .type(Person)
                .property("name", ElementType.FIELD)
                    .constraint(new NotEmptyDef().message("name should not be empty"))
                .property("age", ElementType.FIELD)
                    .constraint(new RangeDef()
                        .min(10).max(1000)
                        .message("age should be between {min} and {max}"))

        Validator validator = configuration
                .addMapping(constraintMapping)
                .buildValidatorFactory()
                .validator

        def person = new Person()
        person.name = ''
        person.age = 1001

        def violations = validator.validate(person)
        assertEquals 2, violations.size()

        def nameViolation = violations.find { it.propertyPath.toString() == 'name' }
        assertNotNull nameViolation
        assertEquals 'name', nameViolation.propertyPath.toString()
        assertEquals 'name should not be empty', nameViolation.message

        def ageViolation = violations.find { it.propertyPath.toString() == 'age' }
        assertNotNull ageViolation
        assertEquals 'age', ageViolation.propertyPath.toString()
        assertEquals 'age should be between 10 and 1000', ageViolation.message
    }

    static class Person {
        String name
        int age
    }
}
