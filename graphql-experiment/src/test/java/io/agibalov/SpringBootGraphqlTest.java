package io.agibalov;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
public class SpringBootGraphqlTest {
    @Test
    public void canTalkToMvc() {
        RestTemplate restTemplate = new RestTemplate();
        String s = restTemplate.getForObject("http://localhost:8080/hello", String.class);
        assertEquals("hi there", s);
    }

    @Test
    public void canGetSuccessfulResult() {
        RestTemplate restTemplate = new RestTemplate();
        JsonNode requestBody = JsonNodeFactory.instance.objectNode()
                .putNull("operationName")
                .putNull("variables")
                .put("query", "{ hello(name: \"Andrey\") }");

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "http://localhost:8080/graphql", requestBody, JsonNode.class);
        assertEquals("Hello, Andrey!", responseEntity.getBody().get("data").get("hello").textValue());
    }

    @Test
    public void canGetTypeValidationError() {
        RestTemplate restTemplate = new RestTemplate();
        JsonNode requestBody = JsonNodeFactory.instance.objectNode()
                .putNull("operationName")
                .putNull("variables")
                .put("query", "{ hello(name: 123) }");

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "http://localhost:8080/graphql", requestBody, JsonNode.class);

        JsonNode responseBody = responseEntity.getBody();
        assertTrue(responseBody.get("data").isNull());

        JsonNode errors = responseBody.get("errors");
        assertEquals(1, errors.size());
        assertEquals("ValidationError", errors.get(0).get("errorType").textValue());
        assertTrue(errors.get(0).get("message").textValue().contains("is not a valid 'String' @ 'hello'"));
    }

    @Test
    public void canGetFieldUndefinedValidationError() {
        RestTemplate restTemplate = new RestTemplate();
        JsonNode requestBody = JsonNodeFactory.instance.objectNode()
                .putNull("operationName")
                .putNull("variables")
                .put("query", "{ omg }");

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "http://localhost:8080/graphql", requestBody, JsonNode.class);

        JsonNode responseBody = responseEntity.getBody();
        assertTrue(responseBody.get("data").isNull());

        JsonNode errors = responseBody.get("errors");
        assertEquals(1, errors.size());
        assertEquals("ValidationError", errors.get(0).get("errorType").textValue());
        assertTrue(errors.get(0).get("message").textValue().contains("Field 'omg' in type 'Query' is undefined @ 'omg'"));
    }
}
