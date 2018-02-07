package me.loki2302.groups

import org.hibernate.validator.constraints.*

class TellUsAboutYourselfWizardState {
    @NotEmpty(groups = Step1)
    String name

    @Range(min = 10L, max = 1000L, groups = Step1)
    int age

    @NotEmpty(groups = Step2)
    String favoriteProgrammingLanguage
}
