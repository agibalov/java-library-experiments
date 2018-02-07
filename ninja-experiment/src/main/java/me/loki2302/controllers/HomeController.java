package me.loki2302.controllers;

import ninja.Result;
import ninja.Results;

import com.google.inject.Singleton;

@Singleton
public class HomeController {
	public Result index() {
		return Results.html();
	}
	
	public Result angular() {
		return Results.html();
	}
	
	public Result robots() {
		String robotsTxt = 
				"# hi robot\n\n" + 
				"User-agent: *\n" +
				"Disallow: /App\n";
		
		return Results.contentType(Result.TEXT_PLAIN).render(robotsTxt);
	}
}
