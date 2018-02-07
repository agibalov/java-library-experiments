package me.loki2302.controllers;

import ninja.Result;
import ninja.Results;
import ninja.params.Param;

import com.google.inject.Singleton;

@Singleton
public class ApiController {	
	public Result addNumbersGet(
			@Param("a") int a,
			@Param("b") int b) {
		
		AddNumbersResponse addNumbersResponse = new AddNumbersResponse();
		addNumbersResponse.a = a;
		addNumbersResponse.b = b;
		addNumbersResponse.result = a + b;
		
		return Results.json().render(addNumbersResponse);
	}
	
	public Result addNumbersPost(AddNumbersRequest addNumbersRequest) {
		int a = addNumbersRequest.a;
		int b = addNumbersRequest.b;
		
		AddNumbersResponse addNumbersResponse = new AddNumbersResponse();
		addNumbersResponse.a = a;
		addNumbersResponse.b = b;
		addNumbersResponse.result = a + b;
		
		return Results.json().render(addNumbersResponse);
	}
	
	public static class AddNumbersRequest {
		public int a;
		public int b;		
	}
	
	public static class AddNumbersResponse {
		public int a;
		public int b;
		public int result;
	}
}