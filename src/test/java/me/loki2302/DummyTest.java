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
import org.slf4j.MDC;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.assertFalse;
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

    @Test
    public void canUseDifferentLoggingLevels() throws JoranException {
        org.slf4j.Logger debugLogger = LoggerFactory.getLogger("debug-logger");
        org.slf4j.Logger infoLogger = LoggerFactory.getLogger("info-logger");
        org.slf4j.Logger warnLogger = LoggerFactory.getLogger("warn-logger");

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        JoranConfigurator joranConfigurator = new JoranConfigurator();
        joranConfigurator.setContext(loggerContext);
        loggerContext.reset();
        joranConfigurator.doConfigure(DummyTest.class.getResource("/log-levels.xml"));

        debugLogger.debug("debug debug");
        debugLogger.info("debug info");
        debugLogger.warn("debug warn");

        infoLogger.debug("info debug");
        infoLogger.info("info info");
        infoLogger.warn("info warn");

        warnLogger.debug("warn debug");
        warnLogger.info("warn info");
        warnLogger.warn("warn warn");

        assertTrue(systemOutRule.getLog().contains("DEBUG debug debug"));
        assertTrue(systemOutRule.getLog().contains("INFO debug info"));
        assertTrue(systemOutRule.getLog().contains("WARN debug warn"));

        assertFalse(systemOutRule.getLog().contains("DEBUG info debug"));
        assertTrue(systemOutRule.getLog().contains("INFO info info"));
        assertTrue(systemOutRule.getLog().contains("WARN info warn"));

        assertFalse(systemOutRule.getLog().contains("DEBUG warn debug"));
        assertFalse(systemOutRule.getLog().contains("INFO warn info"));
        assertTrue(systemOutRule.getLog().contains("WARN warn warn"));
    }

    @Test
    public void canUseMdc() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        PatternLayoutEncoder patternLayoutEncoder = new PatternLayoutEncoder();
        patternLayoutEncoder.setPattern("%date %level [%thread] %logger{10} [%file:%line] <%X{username}> %msg%n");
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

        MDC.put("username", "loki2302");
        logger.debug("hi there");

        MDC.remove("username");
        logger.debug("hi there");

        String lines[] = new String(baos.toByteArray(), Charset.forName("UTF-8")).split("\n");
        assertTrue(lines[0].contains("<> hi there"));
        assertTrue(lines[1].contains("<loki2302> hi there"));
        assertTrue(lines[2].contains("<> hi there"));
    }
}
