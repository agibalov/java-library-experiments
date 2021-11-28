package io.agibalov;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Controller("calculator")
public class CalculatorController {
    @Inject
    AdderService adderService;

    @Post("add")
    public HttpResponse<AddNumbersResponseBody> addNumbers(@Body AddNumbersRequestBody requestBody) {
        if (requestBody.getA() > 100 || requestBody.getB() > 100) {
            return HttpResponse.badRequest();
        }

        return HttpResponse.ok(AddNumbersResponseBody.builder()
                .result(adderService.addNumbers(requestBody.getA(), requestBody.getB()))
                .build());
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddNumbersRequestBody {
        private int a;
        private int b;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddNumbersResponseBody {
        private int result;
    }
}
