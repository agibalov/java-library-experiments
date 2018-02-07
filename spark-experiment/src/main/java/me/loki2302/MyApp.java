package me.loki2302;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import spark.Request;
import spark.Response;
import spark.servlet.SparkApplication;
import static spark.Spark.*;
import static me.loki2302.ModelAndView.*;

public class MyApp implements SparkApplication {
	@Override
	public void init() {
		get(new FreeMarkerTemplateViewRoute("/") {
			@Override
			protected ModelAndView process(Request request, Response response) {
				UserModel model = new UserModel();
				model.setName("loki2302");
				return modelAndView(model, "index.ftl");
			}			
		});
		
		get(new FreeMarkerTemplateViewRoute("/second") {
			@Override
			public ModelAndView process(Request request, Response response) {
				return view("second.ftl");
			}			
		});
		
		get(new FreeMarkerTemplateViewRoute("/add/:a/:b") {
			@Override
			public ModelAndView process(Request request, Response response) {
				int a = Integer.parseInt(request.params(":a"));
				int b = Integer.parseInt(request.params(":b"));
				
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("a", a);
				model.put("b", b);
				model.put("result", a + b);
				
				return modelAndView(model, "add.ftl");
			}			
		});
		
		get(new JsonRoute("/api/hello/:name") {
			@Override
			protected Object process(Request request, Response response) {
				UserModel userModel = new UserModel();
				userModel.setName(request.params(":name"));
				return userModel;
			}			
		});
		
		get(new FreeMarkerTemplateViewRoute("/angular") {
			@Override
			protected ModelAndView process(Request request, Response response) {
				return view("angular.ftl");
			}			
		});
		
		get(new JsonRoute("/api/addNumbersGet") {
			@Override
			protected Object process(Request request, Response response) {
				AddNumbersResponse addNumbersResponse = new AddNumbersResponse();
				addNumbersResponse.a = Integer.parseInt(request.queryParams("a"));
				addNumbersResponse.b = Integer.parseInt(request.queryParams("b"));
				addNumbersResponse.result = addNumbersResponse.a + addNumbersResponse.b; 
				return addNumbersResponse;
			}			
		});
		
		post(new JsonRoute("/api/addNumbersPost") {
			@Override
			protected Object process(Request request, Response response) {
				Gson gson = new Gson();
				
				AddNumbersRequest addNumbersRequest = 
						gson.fromJson(request.body(), AddNumbersRequest.class);
				
				AddNumbersResponse addNumbersResponse = new AddNumbersResponse();
				addNumbersResponse.a = addNumbersRequest.a;
				addNumbersResponse.b = addNumbersRequest.b;
				addNumbersResponse.result = addNumbersResponse.a + addNumbersResponse.b; 
				return addNumbersResponse;
			}			
		});
	}	
	
	public static class UserModel {
		private String name;
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
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
