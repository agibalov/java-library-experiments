package me.loki2302;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.testcontainers.containers.MySQLContainer;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@TestExecutionListeners(listeners = {
        ExplicitContainerTest.MySqlContainerTestExecutionListener.class
}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:mysql://localhost/test",
        "spring.datasource.username=test",
        "spring.datasource.password=test",
        "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect"
})
@RunWith(SpringRunner.class)
public class ExplicitContainerTest {
    // This doesn't work, because rule gets run after Spring context is constructed,
    // which is too late
    /*@Rule
    public MySQLContainer mySQLContainer = new MySQLContainer();*/

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

    public static class MySqlContainerTestExecutionListener extends AbstractTestExecutionListener implements Ordered {
        private MySQLContainer mySQLContainer;

        @Override
        public void beforeTestClass(TestContext testContext) throws Exception {
            mySQLContainer = new MySQLContainer();
            mySQLContainer.setPortBindings(Arrays.asList("3306:3306"));
            mySQLContainer.start();
        }

        @Override
        public void afterTestClass(TestContext testContext) throws Exception {
            mySQLContainer.stop();
        }

        @Override
        public int getOrder() {
            return 0;
        }
    }
}
