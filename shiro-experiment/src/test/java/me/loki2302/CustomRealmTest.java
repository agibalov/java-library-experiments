package me.loki2302;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class CustomRealmTest {
    @Test
    public void dummy() {
        AuthenticationService authenticationService = new AuthenticationService();
        AuthorizationService authorizationService = new AuthorizationService();
        MyRealm myRealm = new MyRealm(authenticationService, authorizationService);
        DefaultSecurityManager securityManager = new DefaultSecurityManager(myRealm);

        Subject subject = new Subject.Builder(securityManager).buildSubject();
        subject.login(new UsernamePasswordToken("loki2302", "qwerty"));
        assertTrue(subject.isAuthenticated());
        assertTrue(subject.hasRole("admin"));
        assertTrue(subject.hasRole("user"));
    }

    public static class AuthenticationService {
        public boolean isGoodUsernameAndPassword(String username, String password) {
            return username != null && username.equals("loki2302") && password != null && password.equals("qwerty");
        }
    }

    public static class AuthorizationService {
        public boolean isAdmin(String username) {
            return username != null && username.equals("loki2302");
        }

        public boolean isUser(String username) {
            return username != null && username.equals("loki2302");
        }
    }

    public static class MyRealm extends AuthorizingRealm {
        private final AuthenticationService authenticationService;
        private final AuthorizationService authorizationService;

        public MyRealm(AuthenticationService authenticationService, AuthorizationService authorizationService) {
            this.authenticationService = authenticationService;
            this.authorizationService = authorizationService;
        }

        @Override
        protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
            String username = (String)principals.getPrimaryPrincipal();

            Set<String> roles = new HashSet<String>();

            boolean isAdmin = authorizationService.isAdmin(username);
            if(isAdmin) {
                roles.add("admin");
            }

            boolean isUser = authorizationService.isUser(username);
            if(isUser) {
                roles.add("user");
            }

            SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo(roles);
            return authorizationInfo;
        }

        @Override
        protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
            if(!(token instanceof UsernamePasswordToken)) {
                throw new IllegalArgumentException("token should be of type " + UsernamePasswordToken.class.getName());
            }

            UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken)token;
            String username = usernamePasswordToken.getUsername();
            String password = new String(usernamePasswordToken.getPassword());
            boolean usernameAndPasswordAreOk = authenticationService.isGoodUsernameAndPassword(username, password);
            if(!usernameAndPasswordAreOk) {
                return null;
            }

            Object principal = username;
            Object credentials = password;
            SimpleAuthenticationInfo authenticationInfo =
                    new SimpleAuthenticationInfo(principal, credentials, "myRealm");

            return authenticationInfo;
        }
    }
}
