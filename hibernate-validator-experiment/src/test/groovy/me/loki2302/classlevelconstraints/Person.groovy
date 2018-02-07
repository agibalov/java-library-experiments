package me.loki2302.classlevelconstraints

import groovy.transform.builder.Builder

@GoodPerson
@Builder
class Person {
    boolean likesProgramming
    int knowsLanguages
}
