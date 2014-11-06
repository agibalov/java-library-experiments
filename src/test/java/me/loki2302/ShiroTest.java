package me.loki2302;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.apache.shiro.util.LifecycleUtils;
import org.apache.shiro.util.ThreadState;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ShiroTest {
    private static ThreadState subjectThreadState;

    @Test
    public void canDoSomething() {
        SimpleAccountRealm myRealm = new SimpleAccountRealm("My Realm");
        myRealm.addRole("admin");
        myRealm.addAccount("loki2302", "qwerty", "admin");

        DefaultSecurityManager securityManager = new DefaultSecurityManager(myRealm);
        SecurityUtils.setSecurityManager(securityManager);

        Subject subject = new Subject.Builder(SecurityUtils.getSecurityManager()).buildSubject();
        setSubject(subject);

        Subject currentUser = SecurityUtils.getSubject();
        assertFalse(currentUser.isAuthenticated());

        UsernamePasswordToken token = new UsernamePasswordToken("loki2302", "qwerty");
        currentUser.login(token);
        assertTrue(currentUser.isAuthenticated());

        assertTrue(currentUser.hasRole("admin"));
        assertFalse(currentUser.hasRole("user"));
    }

    @After
    public void cleanUpShiro() {
        clearSubject();
        SecurityManager securityManager = SecurityUtils.getSecurityManager();
        LifecycleUtils.destroy(securityManager);
        SecurityUtils.setSecurityManager(null);
    }

    private static void setSubject(Subject subject) {
        clearSubject();
        subjectThreadState = new SubjectThreadState(subject);
        subjectThreadState.bind();
    }

    private static void clearSubject() {
        if(subjectThreadState != null) {
            subjectThreadState.clear();
            subjectThreadState = null;
        }
    }
}
