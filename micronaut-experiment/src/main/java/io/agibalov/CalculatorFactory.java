package io.agibalov;

import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;

@Factory
public class CalculatorFactory {
    @Singleton
    AdderService adderService() {
        return new AdderService();
    }
}
