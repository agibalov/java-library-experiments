package me.loki2302.metrics.impl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotating a method with {@link AfterIteration} makes JUnit
 * execute this method after every benchmark iteration. JUnit's
 * standard {@link org.junit.After} annotation on the other hand
 * makes JUnit execute the method after every test method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AfterIteration {
}
