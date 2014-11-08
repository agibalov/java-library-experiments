package me.loki2302.objectgraphvalidation

import groovy.transform.builder.Builder
import org.hibernate.validator.constraints.NotEmpty

import javax.validation.Valid
import javax.validation.constraints.NotNull

@Builder
class Person {
    @NotEmpty
    String name

    @NotNull
    @Valid
    PersonDetails details

    @NotEmpty
    @Valid
    List<Interest> interests
}
