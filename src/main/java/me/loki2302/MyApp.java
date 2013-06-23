package me.loki2302;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.servlet.SparkApplication;
import static spark.Spark.*;

public class MyApp implements SparkApplication {
	@Override
	public void init() {
		get(new Route("/") {
			@Override
			public Object handle(Request request, Response response) {
				UserModel model = new UserModel();
				model.setName("loki2302");
				return render("index.ftl", model);
			}			
		});
		
		get(new Route("/second") {
			@Override
			public Object handle(Request request, Response response) {
				return render("second.ftl");
			}			
		});
		
		get(new Route("/add/:a/:b") {
			@Override
			public Object handle(Request request, Response response) {
				int a = Integer.parseInt(request.params(":a"));
				int b = Integer.parseInt(request.params(":b"));
				
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("a", a);
				model.put("b", b);
				model.put("result", a + b);
				
				return render("add.ftl", model);
			}			
		});
	}
	
	private String render(String viewName, Object model) {
		Configuration configuration = new Configuration();
		configuration.setClassForTemplateLoading(MyApp.class, "/freemarker/"); 
		try {
			Template template = configuration.getTemplate(viewName);
			StringWriter stringWriter = new StringWriter();					
	    	template.process(model, stringWriter);
	    	return stringWriter.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		
		throw new RuntimeException("failed to render template for some reason");
	}
	
	private String render(String viewName) {
		return render(viewName, null);
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
