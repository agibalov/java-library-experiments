package me.loki2302.metrics.impl;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * A specialization of {@link BlockJUnit4ClassRunner} that allows running tests in 2 modes:
 * "tests only" and "benchmarks". The mode is determined based on system property ENABLE_BENCHMARK -
 * as long as it's not set or its value is empty, tests are run in "tests only" mode. Otherwise they
 * are run in "benchmarks" mode.
 *
 * In "tests only" mode, the execution flow is like this:
 *
 * For every test method...
 * <ul>
 *     <li>@Before methods x 1</li>
 *     <li>@BeforeIteration methods x 1</li>
 *     <li>@Test method itself x 1</li>
 *     <li>@AfterIteration methods x 1</li>
 *     <li>@After methods x 1</li>
 * </ul>
 *
 * In "benchmarks" mode, the execution flow is like this:
 *
 * For every test method...
 * <ul>
 *     <li>@Before methods x 1</li>
 *     <li>@BeforeIteration methods x (WARMUP_IT + MEASUREMENT_IT)</li>
 *     <li>@Test method itself x (WARMUP_IT + MEASUREMENT_IT)</li>
 *     <li>@AfterIteration methods x (WARMUP_IT + MEASUREMENT_IT)</li>
 *     <li>@After methods x 1</li>
 * </ul>
 *
 * @see AfterIteration
 * @see BeforeIteration
 * @see Benchmark
 */
public class FancyRunner extends BlockJUnit4ClassRunner {
    private final static FancyRunnerImplementation fancyRunnerImplementation = new FancyRunnerImplementation();

    public FancyRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        Statement statement = super.methodInvoker(method, test);
        statement = fancyRunnerImplementation.decorateStatement(getTestClass(), statement, method, test);
        return statement;
    }
}
