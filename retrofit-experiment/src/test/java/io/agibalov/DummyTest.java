package io.agibalov;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DummyTest {
    @Test
    public void dummy() throws IOException {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(s -> log.info(s))
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    log.info("Before {} {}", request.method(), request.url().toString());

                    okhttp3.Response response = null;
                    try {
                        response = chain.proceed(request);
                        return response;
                    } finally {
                        BufferedSource source = response.body().source();
                        source.request(Long.MAX_VALUE);
                        String bodyString = source.getBuffer().clone().readString(StandardCharsets.UTF_8);

                        log.info("After {} {} (response={} {})", request.method(), request.url().toString(),
                                response.code(),
                                bodyString);
                    }
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://localhost:8080/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(new ObjectMapper()))
                .build();
        DummyService dummyService = retrofit.create(DummyService.class);

        assertEquals("http://localhost:8080/hello",
                dummyService.getHelloAsString().request().url().toString());

        Response<String> stringResponse = dummyService.getHelloAsString().execute();
        assertEquals(200, stringResponse.code());
        assertEquals("{\"message\":\"hello world\"}", stringResponse.body());

        Response<okhttp3.ResponseBody> responseBodyResponse = dummyService.getHelloAsResponseBody().execute();
        assertEquals(200, responseBodyResponse.code());
        assertEquals("{\"message\":\"hello world\"}", responseBodyResponse.body().string());

        Response<Message> dtoResponse = dummyService.getHelloAsDto().execute();
        assertEquals(200, dtoResponse.code());
        assertEquals(new Message("hello world"), dtoResponse.body());

        assertEquals("http://localhost:8080/add/2/3",
                dummyService.addViaPathVariables(2, 3).request().url().toString());
        assertEquals(new Message("5"), dummyService.addViaPathVariables(2, 3).execute().body());

        assertEquals("http://localhost:8080/add?a=2&b=3",
                dummyService.addViaQueryParams(2, 3).request().url().toString());
        assertEquals(new Message("5"), dummyService.addViaQueryParams(2, 3).execute().body());

        assertEquals("http://localhost:8080/add-headers",
                dummyService.addViaHeaders(2, 3).request().url().toString());
        assertEquals(new Message("5"), dummyService.addViaHeaders(2, 3).execute().body());

        assertEquals(new Message("5"), dummyService.addViaPost(TwoNumbers.builder()
                .a(2)
                .b(3)
                .build()).execute().body());
    }

    public interface DummyService {
        @GET("/hello")
        Call<String> getHelloAsString();

        @GET("/hello")
        Call<okhttp3.ResponseBody> getHelloAsResponseBody();

        @GET("/hello")
        Call<Message> getHelloAsDto();

        @GET("/add/{a}/{b}")
        Call<Message> addViaPathVariables(@Path("a") int a, @Path("b") int b);

        @GET("/add")
        Call<Message> addViaQueryParams(@Query("a") int a, @Query("b") int b);

        @GET("/add-headers")
        Call<Message> addViaHeaders(@Header("a") int a, @Header("b") int b);

        @POST("/add")
        Call<Message> addViaPost(@Body TwoNumbers twoNumbers);
    }

    @Configuration
    @SpringBootApplication
    public static class Config {
        @Bean
        public DummyController dummyController() {
            return new DummyController();
        }
    }

    @ResponseBody
    @RequestMapping
    public static class DummyController {
        @GetMapping("/hello")
        public ResponseEntity<?> hello() {
            return ResponseEntity.ok(Message.builder()
                    .message("hello world")
                    .build());
        }

        @GetMapping("/add/{a}/{b}")
        public ResponseEntity<?> addViaPathVariables(
                @PathVariable("a") int a,
                @PathVariable("b") int b) {

            return ResponseEntity.ok(Message.builder()
                    .message(String.format("%d", a + b))
                    .build());
        }

        @GetMapping("/add")
        public ResponseEntity<?> addViaQueryParams(
                @RequestParam("a") int a,
                @RequestParam("b") int b) {

            return ResponseEntity.ok(Message.builder()
                    .message(String.format("%d", a + b))
                    .build());
        }

        @GetMapping("/add-headers")
        public ResponseEntity<?> addViaHeaders(
                @RequestHeader("a") int a,
                @RequestHeader("b") int b) {

            return ResponseEntity.ok(Message.builder()
                    .message(String.format("%d", a + b))
                    .build());
        }

        @PostMapping("/add")
        public ResponseEntity<?> addViaPost(@RequestBody TwoNumbers twoNumbers) {
            return ResponseEntity.ok(Message.builder()
                    .message(String.format("%d", twoNumbers.getA() + twoNumbers.getB()))
                    .build());
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Message {
        private String message;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TwoNumbers {
        private int a;
        private int b;
    }
}
