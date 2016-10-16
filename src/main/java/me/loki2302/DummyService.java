package me.loki2302;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DummyService {
    private final static Logger LOGGER = LoggerFactory.getLogger(DummyService.class);

    public void doSomething() {
        LOGGER.info("I am doing something");
    }
}
