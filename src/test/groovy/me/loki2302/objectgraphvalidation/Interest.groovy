package me.loki2302.objectgraphvalidation

import groovy.transform.builder.Builder
import org.hibernate.validator.constraints.NotEmpty

@Builder
class Interest {
    @NotEmpty
    String description
}
