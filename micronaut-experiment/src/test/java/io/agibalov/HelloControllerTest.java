package io.agibalov;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class HelloControllerTest {
    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void testHelloResponse() {
        HttpResponse<String> response = client.toBlocking().exchange(HttpRequest.GET("/hello"), String.class);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertTrue(response.body().startsWith("Hello world"));
    }
}
