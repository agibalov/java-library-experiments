package me.loki2302.customconstraint

import javax.validation.Constraint
import javax.validation.Payload
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Target([ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE])
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GoodNameConstraintValidator.class)
@interface GoodName {
    String message() default "omg"
    Class<?>[] groups() default []
    Class<? extends Payload>[] payload() default []
    String shouldContain() default "a"
}