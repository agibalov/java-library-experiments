package me.loki2302.metrics.impl;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParameters;
import org.junit.runners.parameterized.TestWithParameters;

/**
 * A version of {@link FancyRunner} that works with {@link org.junit.runners.Parameterized}
 *
 * <code>
 * @RunWith(Parameterized.class)
 * @Parameterized.UseParametersRunnerFactory(FancyRunnerWithParametersFactory.class)
 * public class ParameterizedSpringTest {
 *   // an ordinary parameterized test here
 *   ...
 * </code>
 */
public class FancyRunnerWithParameters extends BlockJUnit4ClassRunnerWithParameters {
    private final static FancyRunnerImplementation fancyRunnerImplementation = new FancyRunnerImplementation();

    public FancyRunnerWithParameters(TestWithParameters test) throws InitializationError {
        super(test);
    }

    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        Statement statement = super.methodInvoker(method, test);
        statement = fancyRunnerImplementation.decorateStatement(getTestClass(), statement, method, test);
        return statement;
    }
}
