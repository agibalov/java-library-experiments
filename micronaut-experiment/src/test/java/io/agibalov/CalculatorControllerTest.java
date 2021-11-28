package io.agibalov;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class CalculatorControllerTest {
    @Inject
    @Client("/")
    HttpClient client;

    @Test
    public void canAdd() {
        HttpResponse<CalculatorController.AddNumbersResponseBody> response = client.toBlocking()
                .exchange(
                        HttpRequest.POST(
                                "/calculator/add",
                                CalculatorController.AddNumbersRequestBody.builder()
                                        .a(2)
                                        .b(3)
                                        .build()
                        ),
                        CalculatorController.AddNumbersResponseBody.class);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(5, response.body().getResult());
    }

    @Test
    public void cantAddBigNumbers() {
        HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking()
                    .exchange(
                            HttpRequest.POST(
                                    "/calculator/add",
                                    CalculatorController.AddNumbersRequestBody.builder()
                                            .a(101)
                                            .b(3)
                                            .build()
                            ),
                            CalculatorController.AddNumbersResponseBody.class);
        });
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
    }
}
