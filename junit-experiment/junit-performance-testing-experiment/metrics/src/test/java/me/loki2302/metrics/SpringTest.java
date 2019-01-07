package me.loki2302.metrics;

import me.loi2302.app.App;
import me.loki2302.metrics.impl.AfterIteration;
import me.loki2302.metrics.impl.BeforeIteration;
import me.loki2302.metrics.impl.Benchmark;
import me.loki2302.metrics.impl.FancyRunner;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.web.client.RestTemplate;

@RunWith(FancyRunner.class)
@FancyTest
public class SpringTest {
    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private RestTemplate restTemplate;

    @Before
    public void before() {
        System.out.printf("before test\n");
    }

    @BeforeIteration
    public void beforeIteration() {
        System.out.printf("before iteration\n");
    }

    @Test
    @Benchmark(warmup = 5, measure = 10, expect = 0.4)
    public void testGetNote() {
        restTemplate.getForObject("http://localhost:8080/api/notes/1", App.NoteDto.class);
    }

    @Test
    @Benchmark(warmup = 5, measure = 10, expect = 0.6)
    public void testDeleteNote() {
        restTemplate.delete("http://localhost:8080/api/notes/1");
    }

    @AfterIteration
    public void afterIteration() {
        System.out.printf("after iteration\n");
    }

    @After
    public void after() {
        System.out.printf("after test\n");
    }
}
