package me.loki2302;

import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static me.loki2302.db.Tables.*;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static org.junit.Assert.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@RunWith(SpringRunner.class)
public class DummyTest {
    @Autowired
    private DSLContext dslContext;

    @Test
    public void canDoWithGeneratedCode() {
        dslContext
                .insertInto(NOTES, NOTES.TEXT)
                .values("hello")
                .execute();

        Result<Record2<Integer, String>> results = dslContext
                .select(NOTES.ID, NOTES.TEXT)
                .from(NOTES)
                .fetch();

        assertEquals(1, (int)results.getValue(0, NOTES.ID));
        assertEquals("hello", results.getValue(0, NOTES.TEXT));
    }

    @Test
    public void canDoWithoutGeneratedCode() {
        dslContext
                .insertInto(table("notes"), field("text"))
                .values("hello")
                .execute();

        Result<Record2<Integer, String>> results = dslContext
                .select(field("id", Integer.class), field("text", String.class))
                .from("notes")
                .fetch();

        assertEquals(1, (int)results.getValue(0, "id"));
        assertEquals("hello", results.getValue(0, "text"));
    }

    @Configuration
    @EnableAutoConfiguration
    public static class Config {
    }
}
