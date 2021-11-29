package io.agibalov;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.startsWith;

@QuarkusTest
public class HelloResourceTest {
    @Test
    public void testIndexResponse() {
        given()
                .when().get("/")
                .then()
                .statusCode(200)
                .body(startsWith("this is the index page"));
    }

    @Test
    public void testHelloResponse() {
        given()
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body(startsWith("Hello world"));
    }
}
