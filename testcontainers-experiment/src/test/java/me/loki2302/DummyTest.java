package me.loki2302;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * Take a look at spring.datasource.url -- the container gets started secretly
 */
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:tc:mysql://ignored/ignored",
        "spring.datasource.username=ignored",
        "spring.datasource.password=ignored",
        "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect"
})
@RunWith(SpringRunner.class)
public class DummyTest {
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
}
