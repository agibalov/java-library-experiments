package io.agibalov;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("calculator")
public class CalculatorResource {
    @Inject
    AdderService adderService;

    @POST
    @Path("add")
    public Response addNumbers(AddNumbersRequestBody requestBody) {
        if (requestBody.getA() > 100 || requestBody.getB() > 100) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.ok(
                AddNumbersResponseBody.builder()
                        .result(adderService.addNumbers(requestBody.getA(), requestBody.getB()))
                        .build())
                .build();
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
