package me.loki2302.groups

import org.junit.Before
import org.junit.Test

import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory

import static org.junit.Assert.*

class ValidationGroupsTest {
    Validator validator

    @Before
    void initValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()
        validator = validatorFactory.getValidator()
    }

    @Test
    void canUseValidationGroups() {
        def wizard = new TellUsAboutYourselfWizardState()
        assertViolationCount(0, wizard)
        assertViolationCount(2, wizard, Step1)
        assertViolationCount(3, wizard, Step1, Step2)

        wizard.name = 'loki2302'
        wizard.age = 100
        assertViolationCount(0, wizard)
        assertViolationCount(0, wizard, Step1)
        assertViolationCount(1, wizard, Step1, Step2)

        wizard.favoriteProgrammingLanguage = 'groovy'
        assertViolationCount(0, wizard)
        assertViolationCount(0, wizard, Step1)
        assertViolationCount(0, wizard, Step1, Step2)
    }

    void assertViolationCount(int count, def wizard, Class... groups) {
        def violations = validator.validate(wizard, groups)
        assertEquals count, violations.size()
    }
}
