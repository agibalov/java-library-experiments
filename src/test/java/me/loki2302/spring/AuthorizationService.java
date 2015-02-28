package me.loki2302.spring;

import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    public boolean isAuthorized(String username) {
        return username.equals("loki2302");
    }
}
