package me.loki2302;

import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.config.MysqldConfig;
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

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v5_7_latest;
import static org.junit.Assert.assertEquals;

@TestExecutionListeners(listeners = {
        WixEmbeddedMySqlTest.WixEmbeddedMySqlTestExecutionListener.class
}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:mysql://localhost/xxx",
        "spring.datasource.username=myuser",
        "spring.datasource.password=mypassword",
        "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.dialect=org.hibernate.dialect.MySQL5Dialect"
})
@RunWith(SpringRunner.class)
public class WixEmbeddedMySqlTest {
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

    public static class WixEmbeddedMySqlTestExecutionListener extends AbstractTestExecutionListener {
        private EmbeddedMysql embeddedMysql;

        @Override
        public void beforeTestClass(TestContext testContext) throws Exception {
            MysqldConfig mysqldConfig = aMysqldConfig(v5_7_latest)
                    .withUser("myuser", "mypassword")
                    .withPort(3306)
                    .build();

            embeddedMysql = anEmbeddedMysql(mysqldConfig)
                    .addSchema("xxx")
                    .start();
        }

        @Override
        public void afterTestClass(TestContext testContext) throws Exception {
            embeddedMysql.stop();
        }

        @Override
        public int getOrder() {
            return 0;
        }
    }
}
