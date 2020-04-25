package io.agibalov;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EventBusTest {
    @Test
    public void canUseEventBus() {
        UserTracker userTracker = new UserTracker();

        EventBus eventBus = new EventBus();
        eventBus.register(userTracker);

        User user = new User(eventBus);

        user.setUsername("loki2302");
        assertEquals(1, userTracker.events.size());
        assertEquals("username: null->loki2302", userTracker.events.get(0));

        user.setPassword("qwerty");
        assertEquals(2, userTracker.events.size());
        assertEquals("password: null->qwerty", userTracker.events.get(1));

        user.setUsername("andrey");
        assertEquals(3, userTracker.events.size());
        assertEquals("username: loki2302->andrey", userTracker.events.get(2));
    }

    public static class UserTracker {
        public final List<String> events = new ArrayList<String>();

        @Subscribe
        public void trackUsernameChange(UsernameHasChangedEvent e) {
            events.add(String.format("username: %s->%s", e.oldUsername, e.username));
        }

        @Subscribe
        public void trackPasswordChange(PasswordHasChangedEvent e) {
            events.add(String.format("password: %s->%s", e.oldPassword, e.password));
        }
    }

    public static class User {
        private final EventBus eventBus;
        private String username;
        private String password;

        public User(EventBus eventBus) {
            this.eventBus = eventBus;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            UsernameHasChangedEvent e = new UsernameHasChangedEvent();
            e.oldUsername = this.username;
            e.username = username;
            eventBus.post(e);

            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            PasswordHasChangedEvent e = new PasswordHasChangedEvent();
            e.oldPassword = this.password;
            e.password = password;
            eventBus.post(e);

            this.password = password;
        }
    }

    public static class UsernameHasChangedEvent {
        public String username;
        public String oldUsername;
    }

    public static class PasswordHasChangedEvent {
        public String password;
        public String oldPassword;
    }
}
