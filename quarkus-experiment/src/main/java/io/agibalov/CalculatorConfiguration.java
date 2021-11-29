package io.agibalov;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class CalculatorConfiguration {
    @Produces
    public AdderService adderService() {
        return new AdderService();
    }
}
