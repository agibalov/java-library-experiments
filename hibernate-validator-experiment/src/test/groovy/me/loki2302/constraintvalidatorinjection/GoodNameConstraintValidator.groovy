package me.loki2302.constraintvalidatorinjection

import com.google.inject.Inject

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class GoodNameConstraintValidator implements ConstraintValidator<GoodName, String> {
    @Inject
    MyValidationService myValidationService

    @Override
    void initialize(GoodName constraintAnnotation) {
    }

    @Override
    boolean isValid(String value, ConstraintValidatorContext context) {
        return myValidationService.isGoodName(value)
    }
}
