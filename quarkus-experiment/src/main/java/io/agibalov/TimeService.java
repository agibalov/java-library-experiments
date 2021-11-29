package io.agibalov;

import javax.enterprise.context.ApplicationScoped;
import java.time.Instant;

@ApplicationScoped
public class TimeService {
    public String getTimeString() {
        return Instant.now().toString();
    }
}
