package me.loki2302;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class ImplementingServiceWithJavaScriptTest {
	private JavaScriptContext context;
	private CalculatorService calculatorService;
	
	@Before
	public void initImplementation() throws IOException {
		context = new JavaScriptContext();
		context.extendWithResource("calculator-service.js");
		JavaScriptService javaScriptService = context.getService("calculatorService");        	
		calculatorService = new JavaScriptCalculatorService(javaScriptService);
	}
	
	@After
	public void cleanUp() {
		context.stop();
	}
	
	@Test
	public void test() throws IOException {
		assertEquals(3, calculatorService.addNumbers(1, 2));
		assertEquals(-1, calculatorService.subNumbers(1, 2));
		assertEquals(2, calculatorService.mulNumbers(1, 2));
		assertEquals(0, calculatorService.divNumbers(1, 2));
	}
	
	public static interface CalculatorService {
		int addNumbers(int a, int b);
		int subNumbers(int a, int b);
		int mulNumbers(int a, int b);
		int divNumbers(int a, int b);
	}
	
	public static class JavaScriptCalculatorService implements CalculatorService {
		private final JavaScriptService javaScriptService;
		
		public JavaScriptCalculatorService(JavaScriptService javaScriptService) {
			this.javaScriptService = javaScriptService;
		}
		
		@Override
		public int addNumbers(int a, int b) {
			return javaScriptService.invoke("addNumbers", int.class, a, b);
		}
		
		@Override
		public int subNumbers(int a, int b) {
			return javaScriptService.invoke("subNumbers", int.class, a, b);
		}
		
		@Override
		public int mulNumbers(int a, int b) {
			return javaScriptService.invoke("mulNumbers", int.class, a, b);
		}
		
		@Override
		public int divNumbers(int a, int b) {
			return javaScriptService.invoke("divNumbers", int.class, a, b);
		}
	}
	
	public static class JavaScriptService {
		private final Context context;
		private final Scriptable scope;
		private final Scriptable serviceScriptable;
				
		public JavaScriptService(
				Context context,
				Scriptable scope,
				Scriptable serviceScriptable) {
			this.context = context;
			this.scope = scope;
			this.serviceScriptable = serviceScriptable;
		}
		
		public <T> T invoke(String functionName, Class<T> returningClass, Object... args) {
			Function function = (Function)ScriptableObject.getProperty(serviceScriptable, functionName);
			Object result = function.call(context, scope, serviceScriptable, args);
			return (T)Context.jsToJava(result, returningClass);
		}
	}
	
	public static class JavaScriptContext {
		private Context context;
		private Scriptable scope;
		
		public JavaScriptContext() {
			context = Context.enter();
			scope = context.initStandardObjects();
		}
		
		public void stop() {
			Context.exit();
		}
		
		public void extendWithResource(String resourceName) throws IOException {
			URL resourceUrl = Resources.getResource(resourceName);
			String resourceContents = Resources.toString(resourceUrl, Charsets.UTF_8);
			context.evaluateString(scope, resourceContents, resourceName, 1, null);
		}
		
		public JavaScriptService getService(String serviceName) {
			Scriptable serviceScriptable = (Scriptable)scope.get(serviceName, scope);
        	
        	JavaScriptService javaScriptService = new JavaScriptService(
        			context, 
        			scope, 
        			serviceScriptable);
        	
        	return javaScriptService;
		}
	}
}
