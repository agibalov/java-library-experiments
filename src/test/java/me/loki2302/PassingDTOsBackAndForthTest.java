package me.loki2302;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

// Pretty the same as PassingScriptablesBackAndForth, but includes
// the fact that I'm not really excited about having all DTOs
// extending ScriptableObject, implementing getters/setters, etc 

public class PassingDTOsBackAndForthTest {
	@Test
	public void canPassDTOToJavaScript() throws JsonGenerationException, JsonMappingException, IOException {		
		Context context = Context.enter();
        try {
        	Scriptable scope = context.initStandardObjects();        	
        	
        	String script = 
        			"function getId(person) { return person.id; }; " + 
        			"function getName(person) { return person.name; }; ";
        	
        	context.evaluateString(scope, script, "test", 1, null);
        	
        	Function getId = (Function)scope.get("getId", scope);
        	Function getName = (Function)scope.get("getName", scope);
        	
        	Person testPerson = new Person();
    		testPerson.id = 123;
    		testPerson.name = "loki2302";
    		Object scriptablePerson = scriptableFromDTO(testPerson, context, scope);
        	
        	Object objId = getId.call(context, scope, null, new Object[] { scriptablePerson });
        	assertEquals(123, Context.jsToJava(objId, int.class));        	
        	
        	Object objName = getName.call(context, scope, null, new Object[] { scriptablePerson });
        	assertEquals("loki2302", Context.jsToJava(objName, String.class));
        } finally {
        	Context.exit();
        }
	}
	
	@Test
	public void canGetDTOFromJavaScript() throws JsonGenerationException, JsonMappingException, IOException {
		Context context = Context.enter();
        try {
        	Scriptable scope = context.initStandardObjects();        	
        	
        	String script =
        			"function withChangedName(person) { person.name = 'xxx'; return person; }; ";
        	
        	context.evaluateString(scope, script, "test", 1, null);
        	
        	Function withChangedName = (Function)scope.get("withChangedName", scope);
        	
        	Person testPerson = new Person();
    		testPerson.id = 123;
    		testPerson.name = "loki2302";
    		Object scriptablePerson = scriptableFromDTO(testPerson, context, scope);
        	        	
        	Object objPerson = withChangedName.call(context, scope, null, new Object[] { scriptablePerson });
        	Person retrievedPerson = dtoFromScriptable(objPerson, Person.class);
        	assertEquals("xxx", retrievedPerson.name);        	
        } finally {
        	Context.exit();
        }
	}
	
	private static Object scriptableFromDTO(Object dto, Context context, Scriptable scope) 
			throws JsonGenerationException, JsonMappingException, IOException {
		
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(dto);
		String expression = String.format("(function(){return %s;})()", json);
		Object scriptable = context.evaluateString(scope, expression, "mapper", 1, null);
		return scriptable;
	}
	
	private static <T> T dtoFromScriptable(Object scriptable, Class<T> clazz) {
		Map<?, ?> objMap = (Map<?, ?>)Context.jsToJava(scriptable, Map.class);
		ObjectMapper mapper = new ObjectMapper();
		T dto = mapper.convertValue(objMap, clazz);
		return dto;
	}
	
	public static class Person {
		public int id;
		public String name;
	}
}
