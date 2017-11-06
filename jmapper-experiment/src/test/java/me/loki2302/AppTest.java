package me.loki2302;

import com.googlecode.jmapper.JMapper;
import com.googlecode.jmapper.annotations.JMap;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AppTest {
    @Test
    public void dummy() {
        Note note = new Note();
        note.id = "123";
        note.name = "hello there";

        JMapper<NoteDto, Note> noteToNoteDtoMapper = new JMapper<>(NoteDto.class, Note.class);

        NoteDto noteDto = noteToNoteDtoMapper.getDestination(note);
        assertEquals(note.id, noteDto.id);
        assertEquals(note.name, noteDto.name);
    }

    // JMapper wants getters and setters
    @Getter
    @Setter
    public static class Note {
        public String id;
        public String name;
    }

    // JMapper wants getters and setters
    @Getter
    @Setter
    public static class NoteDto {
        @JMap
        public String id;
        @JMap
        public String name;
    }
}
