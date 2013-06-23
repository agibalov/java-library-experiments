package me.loki2302;

import java.util.HashMap;
import java.util.Map;

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
}
