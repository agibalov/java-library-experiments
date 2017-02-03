package me.loki2302.jmh;

import me.loi2302.app.App;
import me.loki2302.jmh.impl.Jmh;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.results.RunResult;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class GetNoteBenchmarkTest {
    @Test
    public void getNoteBenchmarkShouldBeOk() {
        RunResult runResult = Jmh.benchmark(GetNoteBenchmarkTest.class, t -> t.benchmarkGetNote(null));
        double mean = runResult.getPrimaryResult().getStatistics().getMean();
        assertThat(mean).isBetween(0.2, 0.4);
    }

    @Benchmark
    public void benchmarkGetNote(AppState appState) {
        RestTemplate restTemplate = appState.restTemplate;
        restTemplate.getForObject("http://localhost:8080/api/notes/1", App.NoteDto.class);
    }
}
