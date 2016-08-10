package me.loki2302;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        Logger logger = LoggerFactory.getLogger(App.class);
        logger.debug("hello world");
    }
}
