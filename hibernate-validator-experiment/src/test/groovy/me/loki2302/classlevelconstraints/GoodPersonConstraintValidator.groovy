package me.loki2302.classlevelconstraints

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class GoodPersonConstraintValidator implements ConstraintValidator<GoodPerson, Person> {
    @Override
    void initialize(GoodPerson constraintAnnotation) {
    }

    @Override
    boolean isValid(Person value, ConstraintValidatorContext context) {
        if(value == null) {
            return false
        }

        context.disableDefaultConstraintViolation()

        def isValid = true

        if(!value.likesProgramming) {
            isValid = false
            context.buildConstraintViolationWithTemplate('{my.custom.template.likesProgramming}')
                    .addPropertyNode('likesProgramming')
                    .addConstraintViolation()
        }

        if(value.knowsLanguages < 2) {
            isValid = false
            context.buildConstraintViolationWithTemplate('{my.custom.template.knowsLanguages}')
                    .addPropertyNode('knowsLanguages')
                    .addConstraintViolation()
        }

        return isValid
    }
}
