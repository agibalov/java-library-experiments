package me.loki2302;

import java.io.IOException;
import java.io.StringWriter;

import spark.Request;
import spark.Response;
import spark.Route;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public abstract class FreeMarkerTemplateViewRoute extends Route {
	protected FreeMarkerTemplateViewRoute(String path) {
		super(path);
	}

	@Override
	public Object handle(Request request, Response response) {
		ModelAndView modelAndView = process(request, response);
		
		Configuration configuration = new Configuration();
		configuration.setClassForTemplateLoading(FreeMarkerTemplateViewRoute.class, "/freemarker/"); 
		try {
			Template template = configuration.getTemplate(modelAndView.getViewName());
			StringWriter stringWriter = new StringWriter();					
	    	template.process(modelAndView.getModel(), stringWriter);
	    	return stringWriter.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		
		throw new RuntimeException("failed to render template for some reason");
	}
	
	protected abstract ModelAndView process(Request request, Response response);
}