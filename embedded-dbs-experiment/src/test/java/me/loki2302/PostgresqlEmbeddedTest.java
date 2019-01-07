package me.loki2302;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;

import static org.junit.Assert.assertEquals;
import static ru.yandex.qatools.embed.postgresql.distribution.Version.Main.V9_6;

@TestExecutionListeners(listeners = {
        PostgresqlEmbeddedTest.EmbeddedPostgresTestExecutionListener.class
}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost/xxx",
        "spring.datasource.username=myuser",
        "spring.datasource.password=mypassword",
        "spring.datasource.driver-class-name=org.postgresql.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.dialect=org.hibernate.dialect.PostgreSQL94Dialect"
})
@RunWith(SpringRunner.class)
public class PostgresqlEmbeddedTest {
    @Autowired
    private NoteRepository noteRepository;

    @Test
    public void dummy() throws InterruptedException {
        assertEquals(0, noteRepository.count());

        Note note = new Note();
        note.text = "hello";

        noteRepository.save(note);

        assertEquals(1, noteRepository.count());
    }

    @Configuration
    @EnableAutoConfiguration
    public static class Config {
    }

    public static class EmbeddedPostgresTestExecutionListener extends AbstractTestExecutionListener {
        private EmbeddedPostgres embeddedPostgres;

        @Override
        public void beforeTestClass(TestContext testContext) throws Exception {
            embeddedPostgres = new EmbeddedPostgres(V9_6);
            embeddedPostgres.start("localhost", 5432, "xxx", "myuser", "mypassword");
        }

        @Override
        public void afterTestClass(TestContext testContext) throws Exception {
            embeddedPostgres.stop();
        }

        @Override
        public int getOrder() {
            return 0;
        }
    }
}
