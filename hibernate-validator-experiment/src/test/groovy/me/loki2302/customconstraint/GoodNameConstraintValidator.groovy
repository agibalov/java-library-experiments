package me.loki2302.customconstraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class GoodNameConstraintValidator implements ConstraintValidator<GoodName, String> {
    String shouldContain

    @Override
    void initialize(GoodName constraintAnnotation) {
        shouldContain = constraintAnnotation.shouldContain()
    }

    @Override
    boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.contains(shouldContain)
    }
}
