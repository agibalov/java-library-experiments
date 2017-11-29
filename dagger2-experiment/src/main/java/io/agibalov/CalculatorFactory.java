package io.agibalov;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = CalculatorModule.class)
interface CalculatorFactory {
    Calculator calculator();
}
