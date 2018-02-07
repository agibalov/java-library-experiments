package me.loki2302;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.guice.ShiroModule;
import org.apache.shiro.guice.aop.ShiroAopModule;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.junit.Test;

import static org.junit.Assert.fail;

public class GuiceAopTest {
    @Test
    public void dummy() {
        Injector injector = Guice.createInjector(new ShiroModule() {
            @Override
            protected void configureShiro() {
                SimpleAccountRealm myRealm = new SimpleAccountRealm("My Realm");
                myRealm.addRole("admin");
                myRealm.addAccount("loki2302", "qwerty", "admin");
                myRealm.addAccount("andrey", "qwerty");

                bindRealm().toInstance(myRealm);
            }
        }, new ShiroAopModule(), new AbstractModule() {
            @Override
            protected void configure() {
                bind(SecuredService.class).asEagerSingleton();
            }
        });

        SecurityManager securityManager = injector.getInstance(SecurityManager.class);
        SecurityUtils.setSecurityManager(securityManager);

        SecuredService securedService = injector.getInstance(SecuredService.class);

        try {
            securedService.getPublicData();
        } catch (UnauthenticatedException e) {
            fail();
        }

        try {
            securedService.getSecretData();
            fail();
        } catch (UnauthenticatedException e) {
        }


        SecurityUtils.getSubject().login(new UsernamePasswordToken("loki2302", "qwerty"));
        try {
            securedService.getPublicData();
        } catch (AuthenticationException e) {
            fail();
        }

        try {
            securedService.getSecretData();
        } catch (AuthenticationException e) {
            fail();
        }
        SecurityUtils.getSubject().logout();


        SecurityUtils.getSubject().login(new UsernamePasswordToken("andrey", "qwerty"));
        try {
            securedService.getPublicData();
        } catch (UnauthorizedException e) {
            fail();
        }

        try {
            securedService.getSecretData();
            fail();
        } catch (UnauthorizedException e) {
        }
        SecurityUtils.getSubject().logout();
    }

    public static class SecuredService {
        @RequiresRoles("admin")
        public String getSecretData() {
            return "secret data";
        }

        public String getPublicData() {
            return "public data";
        }
    }
}
