package me.loki2302;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ShiroTest {
    @Test
    public void canDoSomething() {
        SimpleAccountRealm myRealm = new SimpleAccountRealm("My Realm");
        myRealm.addRole("admin");
        myRealm.addAccount("loki2302", "qwerty", "admin");

        DefaultSecurityManager securityManager = new DefaultSecurityManager(myRealm);

        Subject subject = new Subject.Builder(securityManager).buildSubject();

        assertFalse(subject.isAuthenticated());

        UsernamePasswordToken token = new UsernamePasswordToken("loki2302", "qwerty");
        subject.login(token);
        assertTrue(subject.isAuthenticated());

        assertTrue(subject.hasRole("admin"));
        assertFalse(subject.hasRole("user"));
    }
}
