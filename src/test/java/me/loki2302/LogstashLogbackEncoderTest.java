package me.loki2302;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import static net.logstash.logback.argument.StructuredArguments.keyValue;
import static net.logstash.logback.marker.Markers.append;
import static org.junit.Assert.assertTrue;

public class LogstashLogbackEncoderTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    private Logger logger;

    @Before
    public void configure() throws JoranException {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        JoranConfigurator joranConfigurator = new JoranConfigurator();
        joranConfigurator.setContext(loggerContext);
        loggerContext.reset();
        joranConfigurator.doConfigure(DummyTest.class.getResource("/logstash.xml"));

        logger = LoggerFactory.getLogger("dummy");
    }

    @Test
    public void itShouldWorkInGeneral() throws JoranException {
        logger.info("hello");
        try {
            int i = 1 / 0;
        } catch (Throwable t) {
            logger.error("Omg!", t);
        }
        assertTrue(systemOutRule.getLog().contains("\"logger\":\"dummy\",\"level\":\"INFO\",\"message\":\"hello\",\"exception\":\"\""));
        assertTrue(systemOutRule.getLog().contains("\"logger\":\"dummy\",\"level\":\"ERROR\",\"message\":\"Omg!\",\"exception\":\"java.lang.ArithmeticException: / by zero"));
    }

    @Test
    public void itShouldAllowMeToEnrichLogEntryJsonUsingMdc() {
        try(MDC.MDCCloseable ignore = MDC.putCloseable("something", "123")) {
            logger.info("hello there");
        }
        assertTrue(systemOutRule.getLog().contains("\"message\":\"hello there\""));
        assertTrue(systemOutRule.getLog().contains("\"something\":\"123\""));
    }

    @Test
    public void itShouldAllowMeToEnrichLogEntryJsonUsingMarkers() {
        logger.info(append("something", 123), "hello there");
        assertTrue(systemOutRule.getLog().contains("\"message\":\"hello there\""));
        assertTrue(systemOutRule.getLog().contains("\"something\":123"));
    }

    @Test
    public void itShouldAllowMeToEnrichLogEntryJsonUsingStructuredArguments() {
        logger.info("hello there", keyValue("something", 123));
        assertTrue(systemOutRule.getLog().contains("\"message\":\"hello there\""));
        assertTrue(systemOutRule.getLog().contains("\"something\":123"));
    }
}
