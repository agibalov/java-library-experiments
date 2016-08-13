package me.loki2302;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.joran.spi.JoranException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DummyTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Test
    public void canConfigureLoggingAtRuntime() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        PatternLayoutEncoder patternLayoutEncoder = new PatternLayoutEncoder();
        patternLayoutEncoder.setPattern("%date %level [%thread] %logger{10} [%file:%line] %msg%n");
        patternLayoutEncoder.setContext(loggerContext);
        patternLayoutEncoder.start();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamAppender<ILoggingEvent> outputStreamAppender = new OutputStreamAppender<>();
        outputStreamAppender.setContext(loggerContext);
        outputStreamAppender.setEncoder(patternLayoutEncoder);
        outputStreamAppender.setOutputStream(baos);
        outputStreamAppender.start();

        Logger logger = loggerContext.getLogger(App.class);
        logger.addAppender(outputStreamAppender);
        logger.setLevel(Level.DEBUG);
        logger.setAdditive(false);
        logger.debug("hi there");
        logger.debug("hi omg");

        String loggedString = new String(baos.toByteArray(), Charset.forName("UTF-8"));

        assertTrue(loggedString.contains("\n"));
        assertTrue(loggedString.contains("hi there"));
        assertTrue(loggedString.contains("hi omg"));
    }

    @Test
    public void canUseCustomConfigurationFile() throws JoranException {
        org.slf4j.Logger logger = LoggerFactory.getLogger("dummy");

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        JoranConfigurator joranConfigurator = new JoranConfigurator();
        joranConfigurator.setContext(loggerContext);
        loggerContext.reset();
        joranConfigurator.doConfigure(DummyTest.class.getResource("/dummy.xml"));

        logger.info("hello");

        assertTrue(systemOutRule.getLog().contains("INFO hello"));
    }
}
