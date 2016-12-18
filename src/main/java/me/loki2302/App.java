package me.loki2302;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @RestController
    @RequestMapping("/api/notes")
    public static class DummyController {
        @RequestMapping(value = "{id}", method = RequestMethod.GET)
        public NoteDto getNote(@PathVariable("id") int id) throws InterruptedException {
            Thread.sleep(300);
            NoteDto noteDto = new NoteDto();
            noteDto.id = id;
            noteDto.text = String.format("Text of note #%d", id);
            return noteDto;
        }

        @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
        public void deleteNote(@PathVariable("id") int id) throws InterruptedException {
            Thread.sleep(500);
        }
    }

    public static class NoteDto {
        public int id;
        public String text;
    }
}
