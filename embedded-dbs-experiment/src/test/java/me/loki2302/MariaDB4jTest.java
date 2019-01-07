package me.loki2302;

import ch.vorburger.mariadb4j.DB;
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

import static org.junit.Assert.assertEquals;

@TestExecutionListeners(listeners = {
        MariaDB4jTest.MariaDB4jTestExecutionListener.class
}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:mysql://localhost/xxx",
        "spring.datasource.username=root",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.dialect=org.hibernate.dialect.MySQL5Dialect"
})
@RunWith(SpringRunner.class)
public class MariaDB4jTest {
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

    public static class MariaDB4jTestExecutionListener extends AbstractTestExecutionListener {
        private DB db;

        @Override
        public void beforeTestClass(TestContext testContext) throws Exception {
            db = DB.newEmbeddedDB(3306);
            // user is root/<nopassword>
            db.start();
            db.run("SET GLOBAL time_zone='+00:00';");
            db.createDB("xxx");
        }

        @Override
        public void afterTestClass(TestContext testContext) throws Exception {
            db.stop();
        }

        @Override
        public int getOrder() {
            return 0;
        }
    }
}
