package me.loki2302;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.rnorth.ducttape.unreliables.Unreliables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.testcontainers.containers.GenericContainer;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@TestExecutionListeners(listeners = {
        CustomContainerTest.CustomMySqlContainerTestExecutionListener.class
}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:mysql://localhost/xxx",
        "spring.datasource.username=root",
        "spring.datasource.password=qwerty",
        "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect"
})
@RunWith(SpringRunner.class)
public class CustomContainerTest {
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

    public static class CustomMySqlContainerTestExecutionListener extends AbstractTestExecutionListener implements Ordered {
        private GenericContainer container;

        @Override
        public void beforeTestClass(TestContext testContext) throws Exception {
            container = new GenericContainer("mysql:latest");
            container.addEnv("MYSQL_ROOT_PASSWORD", "qwerty");
            container.addEnv("MYSQL_DATABASE", "xxx");
            container.setPortBindings(Arrays.asList("3306:3306"));
            container.start();

            DataSource dataSource = DataSourceBuilder.create()
                    .driverClassName("com.mysql.cj.jdbc.Driver")
                    .username("root")
                    .password("qwerty")
                    .url("jdbc:mysql://localhost/xxx")
                    .build();
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            Unreliables.retryUntilSuccess(120, TimeUnit.SECONDS, () -> {
                Thread.sleep(1000);
                return jdbcTemplate.queryForObject("select 1 + 1", Integer.class);
            });
        }

        @Override
        public void afterTestClass(TestContext testContext) throws Exception {
            container.stop();
        }

        @Override
        public int getOrder() {
            return 0;
        }
    }
}
