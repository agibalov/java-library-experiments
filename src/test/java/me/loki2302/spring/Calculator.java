package me.loki2302.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Calculator {
    @Autowired
    private AuthorizationService authorizationService;

    public int addNumbers(String username, int a, int b) {
        System.out.printf("Calculator has authorizationService: %s\n", authorizationService);

        if(!authorizationService.isAuthorized(username)) {
            throw new NotAuthorizedException();
        }

        return a + b;
    }
}
