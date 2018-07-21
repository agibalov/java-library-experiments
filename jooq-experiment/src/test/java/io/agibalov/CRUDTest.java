package io.agibalov;

import io.agibalov.db.tables.records.NotesRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.exception.TooManyRowsException;
import org.jooq.impl.DSL;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static io.agibalov.db.Tables.NOTES;
import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class CRUDTest {
    @Autowired
    private DSLContext dslContext;

    @Test
    public void canCreateAndGetId() {
        NotesRecord record = dslContext
                .insertInto(NOTES, NOTES.TEXT)
                .values("hello")
                .returning(NOTES.ID)
                .fetchOne();
        long id = record.get(NOTES.ID);
        assertNotEquals(-1, id);
    }

    @Test
    public void canGetOneEntireRow() {
        NotesRecord record = dslContext
                .insertInto(NOTES, NOTES.TEXT)
                .values("hello")
                .returning(NOTES.ID)
                .fetchOne();
        long id = record.get(NOTES.ID);

        NotesRecord fetchedRecord = dslContext.selectFrom(NOTES).where(NOTES.ID.eq(id)).fetchOne();
        assertEquals(id, (long)fetchedRecord.getId());
        assertEquals(id, (long)fetchedRecord.get(NOTES.ID));
        assertEquals(id, (long)fetchedRecord.getValue(NOTES.ID));
        assertEquals("hello", fetchedRecord.getText());
        assertEquals("hello", fetchedRecord.get(NOTES.TEXT));
        assertEquals("hello", fetchedRecord.getValue(NOTES.TEXT));
    }

    @Test
    public void canGetSpecificFieldsOfOneRow() {
        NotesRecord record = dslContext
                .insertInto(NOTES, NOTES.TEXT)
                .values("hello")
                .returning(NOTES.ID)
                .fetchOne();
        long id = record.get(NOTES.ID);

        Record fetchedRecord = dslContext.select(NOTES.TEXT)
                .from(NOTES)
                .where(NOTES.ID.eq(id))
                .fetchOne();
        assertEquals(1, fetchedRecord.fieldsRow().size());
        assertEquals("hello", fetchedRecord.get(NOTES.TEXT));
    }

    @Test
    public void canGetNullIfThereIsNoRow() {
        NotesRecord record = dslContext.selectFrom(NOTES).where(NOTES.ID.eq(123L)).fetchOne();
        assertNull(record);
    }

    @Test
    public void canGetExceptionIfThereIsMoreThanOneRow() {
        dslContext.insertInto(NOTES, NOTES.TEXT)
                .values("hello")
                .values("hi there")
                .values("omg")
                .execute();

        try {
            dslContext.selectFrom(NOTES).fetchOne();
            fail();
        } catch (TooManyRowsException e) {
        }
    }

    @Test
    public void canUpdate() {
        dslContext.insertInto(NOTES, NOTES.TEXT)
                .values("hello")
                .values("hi there")
                .values("omg")
                .execute();

        dslContext.update(NOTES)
                .set(NOTES.TEXT, "qwerty")
                .where(NOTES.TEXT.eq("omg"))
                .execute();

        Set<String> texts = dslContext.select(NOTES.TEXT)
                .from(NOTES)
                .fetch()
                .intoSet(NOTES.TEXT);
        assertEquals(3, texts.size());
        assertTrue(texts.contains("qwerty"));
        assertFalse(texts.contains("omg"));
    }

    @Test
    public void canDelete() {
        dslContext.insertInto(NOTES, NOTES.TEXT)
                .values("hello")
                .values("hi there")
                .values("omg")
                .execute();

        dslContext.deleteFrom(NOTES).where(DSL.length(NOTES.TEXT).gt(3)).execute();

        Set<String> texts = dslContext.select(NOTES.TEXT)
                .from(NOTES)
                .fetch()
                .intoSet(NOTES.TEXT);
        assertEquals(1, texts.size());
        assertTrue(texts.contains("omg"));
    }

    @SpringBootApplication
    public static class Config {
    }
}
