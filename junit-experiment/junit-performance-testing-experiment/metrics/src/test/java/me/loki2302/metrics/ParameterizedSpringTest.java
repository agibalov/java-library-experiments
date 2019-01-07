package me.loki2302.metrics;

import me.loi2302.app.App;
import me.loki2302.metrics.impl.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
@Parameterized.UseParametersRunnerFactory(FancyRunnerWithParametersFactory.class)
@FancyTest
public class ParameterizedSpringTest {
    @Parameterized.Parameters(name = "{index}: getNote({0}).text == {1}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { 11, "Text of note #11" },
                { 22, "Text of note #22" }
        });
    }

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private RestTemplate restTemplate;

    private final int noteId;
    private final String expectedNoteText;

    public ParameterizedSpringTest(int noteId, String expectedNoteText) {
        this.noteId = noteId;
        this.expectedNoteText = expectedNoteText;
    }

    @Before
    public void before() {
        System.out.printf("before test %d/%s\n", noteId, expectedNoteText);
    }

    @BeforeIteration
    public void beforeIteration() {
        System.out.printf("before iteration %d/%s\n", noteId, expectedNoteText);
    }

    @Test
    @Benchmark(warmup = 5, measure = 10, expect = 0.4)
    public void testGetNote() {
        App.NoteDto result = restTemplate.getForObject(
                "http://localhost:8080/api/notes/{id}",
                App.NoteDto.class,
                noteId);
        assertEquals(expectedNoteText, result.text);
    }

    @AfterIteration
    public void afterIteration() {
        System.out.printf("after iteration %d/%s\n", noteId, expectedNoteText);
    }

    @After
    public void after() {
        System.out.printf("after test %d/%s\n", noteId, expectedNoteText);
    }
}
