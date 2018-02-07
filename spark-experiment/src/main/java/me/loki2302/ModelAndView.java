package me.loki2302;

public class ModelAndView {
	private final Object model;
	private final String viewName;
	
	public ModelAndView(Object model, String viewName) {
		this.model = model;
		this.viewName = viewName;
	}
	
	public Object getModel() {
		return model;
	}
	
	public String getViewName() {
		return viewName;
	}
	
	public static ModelAndView modelAndView(Object model, String viewName) {
		return new ModelAndView(model, viewName);
	}
	
	public static ModelAndView view(String viewName) {
		return new ModelAndView(null, viewName);
	}
}