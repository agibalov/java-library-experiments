package me.loki2302;

import spark.Request;
import spark.Response;
import spark.Route;

import com.google.gson.Gson;

public abstract class JsonRoute extends Route {
	private final static Gson gson = new Gson();		

	protected JsonRoute(String path) {
		super(path);
	}
	
	@Override
	public Object handle(Request request, Response response) {
		Object model = process(request, response);
		response.header("Content-Type", "application/json");
		return gson.toJson(model);
	}
	
	protected abstract Object process(Request request, Response response);
}