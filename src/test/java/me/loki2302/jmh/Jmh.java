package me.loki2302.jmh;

import org.mockito.MockingDetails;
import org.mockito.invocation.Invocation;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;

public class Jmh {
    /**
     * Run one JMH benchmark identified by a benchmark method.
     *
     * @param clazz a class with benchmark method
     * @param consumer a consumer that calls benchmark method
     * @return benchmark execution result
     */
    public static <T> RunResult benchmark(Class<T> clazz, Consumer<T> consumer) {
        T mock = mock(clazz);
        consumer.accept(mock);

        MockingDetails mockingDetails = mockingDetails(mock);
        Collection<Invocation> invocationCollection = mockingDetails.getInvocations();
        if(invocationCollection.size() != 1) {
            throw new IllegalStateException();
        }

        Invocation invocation = invocationCollection.stream().findFirst().get();
        Method invokedMethod = invocation.getMethod();

        String jmhIncludeRegexp = String.format(
                "%s.%s",
                clazz.getSimpleName(),
                invokedMethod.getName());
        Collection<RunResult> runResults = benchmark(Collections.singletonList(jmhIncludeRegexp));
        if (runResults.size() != 1) {
            throw new RuntimeException();
        }

        return runResults.stream().findAny().get();
    }

    public static Collection<RunResult> benchmark(List<String> jmhIncludeRegexps) {
        OptionsBuilder optionsBuilder = new OptionsBuilder();
        jmhIncludeRegexps.forEach(optionsBuilder::include);

        String jmhResultsPath = System.getProperty("JMH_RESULTS_PATH", null);
        if(jmhResultsPath != null) {
            File jmhResultsFile = new File(jmhResultsPath);
            jmhResultsFile.getParentFile().mkdirs();
            optionsBuilder
                    .result(jmhResultsPath)
                    .resultFormat(ResultFormatType.JSON);
        }

        String jmhOutputPath = System.getProperty("JMH_OUTPUT_PATH", null);
        if(jmhOutputPath != null) {
            File jmhOutputFile = new File(jmhOutputPath);
            jmhOutputFile.getParentFile().mkdirs();
            optionsBuilder
                    .output(jmhOutputPath);
        }

        Options options = optionsBuilder
                .mode(Mode.AverageTime)
                .warmupIterations(3)
                .measurementIterations(3)
                .forks(0)
                .verbosity(VerboseMode.EXTRA)
                .build();

        Collection<RunResult> runResults;
        try {
            runResults = new Runner(options).run();
        } catch (RunnerException e) {
            throw new RuntimeException(e);
        }
        return runResults;
    }
}
