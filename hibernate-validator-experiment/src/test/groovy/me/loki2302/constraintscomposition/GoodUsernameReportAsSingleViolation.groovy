package me.loki2302.constraintscomposition

import org.hibernate.validator.constraints.NotEmpty

import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.ReportAsSingleViolation
import javax.validation.constraints.Pattern
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@NotEmpty
@Pattern(regexp = '^[a-z]*$')
@Target([ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE])
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = [])
@ReportAsSingleViolation
@interface GoodUsernameReportAsSingleViolation {
    String message() default "that's a bad username"
    Class<?>[] groups() default []
    Class<? extends Payload>[] payload() default []
}
