package me.loki2302;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AssistedInjectTest {
    @Test
    public void dummy() {
        Injector injector = Guice.createInjector(new FactoryModuleBuilder().build(UserFactory.class));
        UserFactory userFactory = injector.getInstance(UserFactory.class);
        User user = userFactory.makeUser("loki2302");
        assertEquals("loki2302", user.userName);
        assertNotNull(user.userService);
    }

    public static interface UserFactory {
        User makeUser(String userName);
    }

    public static class User {
        public UserService userService;
        public String userName;

        @Inject
        public User(UserService userService, @Assisted String userName) {
            this.userName = userName;
            this.userService = userService;
        }
    }

    public static class UserService {
    }
}
