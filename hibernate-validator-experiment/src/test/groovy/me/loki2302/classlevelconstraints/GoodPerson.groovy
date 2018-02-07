package me.loki2302.classlevelconstraints

import javax.validation.Constraint
import javax.validation.Payload
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Target([ElementType.TYPE])
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GoodPersonConstraintValidator)
@interface GoodPerson {
    String message() default "omg"
    Class<?>[] groups() default []
    Class<? extends Payload>[] payload() default []
}