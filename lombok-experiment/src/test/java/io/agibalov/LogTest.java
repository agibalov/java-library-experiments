package io.agibalov;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class LogTest {
    @Test
    public void canUseLog() {
        LOGGER.info("doing something");
    }
}
