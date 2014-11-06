package me.loki2302;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;

public class App {
    public static void main(String[] args) {
        SimpleAccountRealm myRealm = new SimpleAccountRealm("My Realm");
        myRealm.addRole("admin");
        myRealm.addAccount("loki2302", "qwerty", "admin");

        DefaultSecurityManager securityManager = new DefaultSecurityManager(myRealm);
        SecurityUtils.setSecurityManager(securityManager);

        Subject currentUser = SecurityUtils.getSubject();
        System.out.println(currentUser);
        System.out.println(currentUser.isAuthenticated());

        UsernamePasswordToken token = new UsernamePasswordToken("loki2302", "qwerty");
        currentUser.login(token);
        System.out.println(currentUser.isAuthenticated());

        System.out.println(currentUser.hasRole("admin"));
        System.out.println(currentUser.hasRole("user"));
    }
}
