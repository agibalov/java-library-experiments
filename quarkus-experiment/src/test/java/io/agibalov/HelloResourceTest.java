package io.agibalov;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.startsWith;

@QuarkusTest
public class HelloResourceTest {
    @ConfigProperty(name = "quarkus.http.test-port")
    int testPort;

    @TestHTTPResource("/")
    String baseUrl;

    @Test
    public void testHelloEndpoint() {
        // baseUrl is http://localhost:8081/ (with trailing slash)


        given()
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body(startsWith("Hello world"));
    }
}
