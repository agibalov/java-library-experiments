package io.agibalov;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class CalculatorModule {
    @Provides
    @Singleton
    public Adder adder() {
        return new Adder();
    }

    @Provides
    @Singleton
    public Subtractor subtractor() {
        return new Subtractor();
    }
}
