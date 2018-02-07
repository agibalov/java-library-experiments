package me.loki2302;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.joran.spi.JoranException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.keyValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LogbackTestingTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(LogbackTestingTest.class);

    @Before
    public void reloadLogbackConfig() throws JoranException {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator joranConfigurator = new JoranConfigurator();
        joranConfigurator.setContext(loggerContext);
        loggerContext.reset();
        joranConfigurator.doConfigure(DummyTest.class.getResource("/logback-testing-test.xml"));
    }

    @Test
    public void dummy() {
        LOGGER.info("Log event one");
        SpyingFacade.reset();
        LOGGER.info("Log event two");
        LOGGER.warn("Log event three");

        List<LogRecord> logRecords = SpyingFacade.getRecords();
        assertEquals(2, logRecords.size());

        assertEquals("INFO", logRecords.get(0).level);
        assertEquals(LogbackTestingTest.class.getName(), logRecords.get(0).logger);
        assertEquals("Log event two", logRecords.get(0).message);

        assertEquals("WARN", logRecords.get(1).level);
        assertEquals(LogbackTestingTest.class.getName(), logRecords.get(1).logger);
        assertEquals("Log event three", logRecords.get(1).message);
    }

    @Test
    public void testUserRegistrationHandler() {
        SpyingFacade.reset();

        UserRegistrationHandler userRegistrationHandler = new UserRegistrationHandler(
                new UserCreator(), new WelcomeEmailSender());
        userRegistrationHandler.registerUser();

        List<LogRecord> logRecords = SpyingFacade.getRecords();
        assertTrue(logRecords.stream().anyMatch(r -> r.message.equals("Registering the user...")));
        assertTrue(logRecords.stream().anyMatch(r -> r.message.equals("Created the user")));
        assertTrue(logRecords.stream().anyMatch(r -> r.message.equals("Sent the welcome email")));
        assertTrue(logRecords.stream().anyMatch(r -> r.message.equals("Finished registering the user")));

        assertTrue(logRecords.stream().anyMatch(r -> r.event instanceof UserRegistrationStartedEvent));
        assertTrue(logRecords.stream().anyMatch(r -> r.event instanceof UserCreatedEvent));
        assertTrue(logRecords.stream().anyMatch(r -> r.event instanceof WelcomeEmailSentEvent));
        assertTrue(logRecords.stream().anyMatch(r -> r.event instanceof UserRegistrationFinishedEvent));
    }

    public static class UserRegistrationHandler {
        private final static Logger LOGGER = LoggerFactory.getLogger(UserRegistrationHandler.class);

        private final UserCreator userCreator;
        private final WelcomeEmailSender welcomeEmailSender;

        public UserRegistrationHandler(
                UserCreator userCreator,
                WelcomeEmailSender welcomeEmailSender) {

            this.userCreator = userCreator;
            this.welcomeEmailSender = welcomeEmailSender;
        }

        public void registerUser() {
            LOGGER.info("Registering the user...", keyValue("event", new UserRegistrationStartedEvent()));
            userCreator.createUser();
            welcomeEmailSender.sendWelcomeEmail();
            LOGGER.info("Finished registering the user", keyValue("event", new UserRegistrationFinishedEvent()));
        }
    }

    public static class UserCreator {
        private final static Logger LOGGER = LoggerFactory.getLogger(UserCreator.class);

        public void createUser() {
            LOGGER.info("Created the user", keyValue("event", new UserCreatedEvent()));
            LOGGER.warn("Something else here...");
        }
    }

    public static class WelcomeEmailSender {
        private final static Logger LOGGER = LoggerFactory.getLogger(WelcomeEmailSender.class);

        public void sendWelcomeEmail() {
            LOGGER.info("Sent the welcome email", keyValue("event", new WelcomeEmailSentEvent()));
        }
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = UserRegistrationStartedEvent.class),
            @JsonSubTypes.Type(value = UserRegistrationFinishedEvent.class),
            @JsonSubTypes.Type(value = UserCreatedEvent.class),
            @JsonSubTypes.Type(value = WelcomeEmailSentEvent.class)
    })
    public interface Event {}
    public static class UserRegistrationStartedEvent implements Event {}
    public static class UserRegistrationFinishedEvent implements Event {}
    public static class UserCreatedEvent implements Event {}
    public static class WelcomeEmailSentEvent implements Event {}

    public static class LogRecord {
        @JsonProperty("@timestamp")
        public String timestamp;

        @JsonProperty("logger_name")
        public String logger;

        public String level;
        public String message;
        public Event event;
    }

    public static class SpyingFacade {
        private static String SPYING_APPENDER_NAME = "SPY";

        public static void reset() {
            SpyingAppender<ILoggingEvent> spyingAppender = getSpyingAppender();
            spyingAppender.reset();
        }

        public static List<LogRecord> getRecords() {
            SpyingAppender<ILoggingEvent> spyingAppender = getSpyingAppender();
            ByteArrayOutputStream byteArrayOutputStream = spyingAppender.getBaos();

            System.out.printf("JSON: %s\n", new String(byteArrayOutputStream.toByteArray()));

            ObjectMapper objectMapper = new ObjectMapper();
            JsonFactory jsonFactory = new JsonFactory();
            byte[] jsonLines = byteArrayOutputStream.toByteArray();
            MappingIterator<LogRecord> logRecordIterator;
            try {
                logRecordIterator = objectMapper.readValues(
                        jsonFactory.createParser(jsonLines),
                        LogRecord.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                return logRecordIterator.readAll();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private static SpyingAppender<ILoggingEvent> getSpyingAppender() {
            ch.qos.logback.classic.Logger logger =
                    ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME));
            SpyingAppender<ILoggingEvent> spyingAppender =
                    (SpyingAppender<ILoggingEvent>)logger.getAppender(SPYING_APPENDER_NAME);
            return spyingAppender;
        }
    }

    public static class SpyingAppender<E> extends OutputStreamAppender<E> {
        private final ByteArrayOutputStream baos =
                new ByteArrayOutputStream();

        public ByteArrayOutputStream getBaos() {
            return baos;
        }

        public void reset() {
            baos.reset();
        }

        @Override
        public void start() {
            reset();
            setOutputStream(baos);
            super.start();
        }
    }
}
