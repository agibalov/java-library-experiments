package io.agibalov;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

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
    public void canTalkToGraphql() {
        RestTemplate restTemplate = new RestTemplate();
        JsonNode requestBody = JsonNodeFactory.instance.objectNode()
                .putNull("operationName")
                .putNull("variables")
                .put("query", "{ hello(name: \"Andrey\") }");

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "http://localhost:8080/graphql", requestBody, JsonNode.class);
        assertEquals("Hello, Andrey!", responseEntity.getBody().get("data").get("hello").textValue());
    }

    @SpringBootApplication
    public static class Config {
        @Bean
        public HelloController helloController() {
            return new HelloController();
        }

        @Bean
        public Query query() {
            return new Query();
        }
    }

    @ResponseBody
    @RequestMapping
    @RestController
    public static class HelloController {
        @GetMapping("/hello")
        public String hello() {
            return "hi there";
        }
    }

    public static class Query implements GraphQLQueryResolver {
        public String hello(String name) {
            return String.format("Hello, %s!", name);
        }
    }
}
