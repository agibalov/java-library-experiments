package io.agibalov;

import jakarta.inject.Singleton;

import java.time.Instant;

@Singleton
public class TimeService {
    public String getTimeString() {
        return Instant.now().toString();
    }
}
