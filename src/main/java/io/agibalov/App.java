package io.agibalov;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public HelloController helloController() {
        return new HelloController();
    }

    @Bean
    public Query query() {
        return new Query();
    }

    @ResponseBody
    @RequestMapping
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
