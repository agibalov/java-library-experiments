package me.loki2302;

import me.loki2302.jmh.Jmh;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.results.RunResult;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteNoteBenchmarkTest {
    @Test
    public void deleteNoteBenchmarkShouldBeOk() {
        RunResult runResult = Jmh.benchmark(DeleteNoteBenchmarkTest.class, t -> t.benchmarkDeleteNote(null));
        double mean = runResult.getPrimaryResult().getStatistics().getMean();
        assertThat(mean).isBetween(0.4, 0.6);
    }

    @Benchmark
    public void benchmarkDeleteNote(AppState appState) {
        RestTemplate restTemplate = appState.restTemplate;
        restTemplate.delete("http://localhost:8080/api/notes/1");
    }
}
