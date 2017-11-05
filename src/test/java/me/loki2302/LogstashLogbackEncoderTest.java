package me.loki2302;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertTrue;

public class LogstashLogbackEncoderTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Test
    public void dummy() throws JoranException {
        org.slf4j.Logger logger = LoggerFactory.getLogger("dummy");

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        JoranConfigurator joranConfigurator = new JoranConfigurator();
        joranConfigurator.setContext(loggerContext);
        loggerContext.reset();
        joranConfigurator.doConfigure(DummyTest.class.getResource("/logstash.xml"));

        logger.info("hello");
        try {
            int i = 1 / 0;
        } catch (Throwable t) {
            logger.error("Omg!", t);
        }
        assertTrue(systemOutRule.getLog().contains("\"logger\":\"dummy\",\"level\":\"INFO\",\"message\":\"hello\",\"exception\":\"\""));
        assertTrue(systemOutRule.getLog().contains("\"logger\":\"dummy\",\"level\":\"ERROR\",\"message\":\"Omg!\",\"exception\":\"java.lang.ArithmeticException: / by zero"));
    }
}
