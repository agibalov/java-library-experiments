package me.loki2302.metrics.impl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotating a test method with {@link Benchmark} marks
 * this method as a benchmarkable method.
 *
 * @see FancyRunner
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Benchmark {
    /**
     * @return the number of warm up iterations to perform
     */
    int warmup();

    /**
     * @return the number of measurement iterations to perform
     */
    int measure();

    /**
     * @return the maximum expected mean execution time
     */
    double expect();
}
