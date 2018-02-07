package me.loki2302;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.flywaydb.core.Flyway;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class MyBatisTests {
    private NoteMapper noteMapper;

    @Before
    public void setUpAndCreateSqlSession() {
        String databaseName = UUID.randomUUID().toString();
        JDBCDataSource dataSource = new JDBCDataSource();
        dataSource.setUrl("jdbc:hsqldb:mem:" + databaseName);

        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.migrate();

        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("dev", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(NoteMapper.class);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        noteMapper = sqlSession.getMapper(NoteMapper.class);
    }

    @Test
    public void canCreateNote() {
        Note note = new Note();
        note.content = "hello";
        noteMapper.insert(note);

        assertNotNull(note.id);
    }

    @Test
    public void canGetAllNotes() {
        noteMapper.insert(makeNote("hello"));
        noteMapper.insert(makeNote("there"));
        noteMapper.insert(makeNote("!!!"));

        List<Note> notes = noteMapper.findAll();
        assertEquals(3, notes.size());
        assertEquals(0L, (long)notes.get(0).id);
        assertEquals("hello", notes.get(0).content);
        assertEquals(1L, (long)notes.get(1).id);
        assertEquals("there", notes.get(1).content);
        assertEquals(2L, (long)notes.get(2).id);
        assertEquals("!!!", notes.get(2).content);
    }

    @Test
    public void canGetOneNote() {
        noteMapper.insert(makeNote("hello"));

        Note thereNote = makeNote("there");
        noteMapper.insert(thereNote);
        noteMapper.insert(makeNote("!!!"));

        Note retrievedThereNote = noteMapper.findOne(thereNote.id);
        assertEquals(thereNote.id, retrievedThereNote.id);
        assertEquals(thereNote.content, retrievedThereNote.content);
    }

    @Test
    public void canGetNullWhenThereIsNoNote() {
        Note note = noteMapper.findOne(123);
        assertNull(note);
    }

    @Test
    public void canUpdateNote() {
        Note note = makeNote("hello");
        noteMapper.insert(note);

        note.content = "hello there";
        noteMapper.update(note);

        note = noteMapper.findOne(note.id);
        assertEquals("hello there", note.content);
    }

    @Test
    public void canDeleteNote() {
        Note note = makeNote("hello");
        noteMapper.insert(note);

        noteMapper.delete(note.id);

        note = noteMapper.findOne(note.id);
        assertNull(note);
    }

    private static Note makeNote(String content) {
        Note note = new Note();
        note.content = content;
        return note;
    }

    public static interface NoteMapper {
        @Insert("insert into Notes(content) values(#{content})")
        @Options(useGeneratedKeys = true, keyProperty = "id")
        void insert(Note note);

        @Select("select * from Notes order by id")
        List<Note> findAll();

        @Select("select * from Notes where id = #{id}")
        Note findOne(long id);

        @Update("update Notes set content = #{content} where id = #{id}")
        void update(Note note);

        @Update("delete from Notes where id = #{id}")
        void delete(long id);
    }

    public static class Note {
        public Long id;
        public String content;
    }
}
