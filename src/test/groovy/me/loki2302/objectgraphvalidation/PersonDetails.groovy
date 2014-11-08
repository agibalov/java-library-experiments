package me.loki2302.objectgraphvalidation

import groovy.transform.builder.Builder
import org.hibernate.validator.constraints.NotEmpty

@Builder
class PersonDetails {
    @NotEmpty
    String about
}
