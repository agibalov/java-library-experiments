package io.agibalov;

public class HelloMessageService {
    private final String helloMessageSuffix;

    public HelloMessageService(String helloMessageSuffix) {
        this.helloMessageSuffix = helloMessageSuffix;
    }

    public String getMessage() {
        return String.format("this is the index page. suffix: %s", helloMessageSuffix);
    }
}
