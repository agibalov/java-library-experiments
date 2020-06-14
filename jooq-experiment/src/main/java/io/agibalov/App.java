package io.agibalov;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

import static io.agibalov.db.Tables.SCHOOLS;
import static io.agibalov.db.Tables.STUDENTS;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @RestController
    @Slf4j
    public static class DummyController {
        private final DSLContext dslContext;

        public DummyController(DSLContext dslContext) {
            this.dslContext = dslContext;
        }

        @GetMapping("/")
        public ResponseEntity<?> index() {
            int schoolCount = dslContext.selectCount().from(SCHOOLS).fetchOne(0, int.class);
            log.info("Schools: {}", schoolCount);

            int studentCount = dslContext.selectCount().from(STUDENTS).fetchOne(0, int.class);
            log.info("Students: {}", studentCount);

            return ResponseEntity.ok(String.format("Hello world %s. Schools: %d, students: %d",
                    Instant.now(),
                    schoolCount,
                    studentCount));
        }
    }
}
