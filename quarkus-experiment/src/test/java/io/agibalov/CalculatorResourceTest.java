package io.agibalov;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class CalculatorResourceTest {
    @Test
    public void canAdd() {
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(CalculatorResource.AddNumbersRequestBody.builder()
                        .a(2)
                        .b(3)
                        .build())
                .post("/calculator/add")
                .then()
                .statusCode(200)
                .body("result", is(5));
    }

    @Test
    public void cantAddBigNumbers() {
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(CalculatorResource.AddNumbersRequestBody.builder()
                        .a(101)
                        .b(3)
                        .build())
                .post("/calculator/add")
                .then()
                .statusCode(400);
    }
}
