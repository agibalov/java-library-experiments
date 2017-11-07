package me.loki2302;

import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

import static org.junit.Assert.assertEquals;

public class SeleniumTest {
    @Rule
    public BrowserWebDriverContainer chrome = new BrowserWebDriverContainer()
            .withDesiredCapabilities(DesiredCapabilities.chrome());

    @Test
    public void dummy() {
        RemoteWebDriver webDriver = chrome.getWebDriver();
        webDriver.get("http://retask.me");
        String title = webDriver.getTitle();
        assertEquals("RETASK", title);
    }
}
