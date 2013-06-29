package me.loki2302;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

public class SimpleEvaluationTest {
	@Test
	public void canExecuteSingleStatementAndReturnResultImmediately() {
		Context context = Context.enter();
        try {
        	Scriptable scope = context.initStandardObjects();
        	Object result = context.evaluateString(scope, "1 + 2", "test", 1, null);
        	assertEquals(3, Context.jsToJava(result, int.class));
        } finally {
        	Context.exit();
        }
	}
	
	@Test
	public void canReadResultsViaScopeAccess() {
		Context context = Context.enter();
        try {
        	Scriptable scope = context.initStandardObjects();
        	context.evaluateString(scope, "var x = 1 + 2; var y = x + 5;", "test", 1, null);
        	
        	Object resultX = scope.get("x", scope);        	
        	assertEquals(3, Context.jsToJava(resultX, int.class));
        	
        	Object resultY = scope.get("y", scope);        	
        	assertEquals(8, Context.jsToJava(resultY, int.class));        	
        } finally {
        	Context.exit();
        }
	}
	
	@Test
	public void canBindToFunctionAndThenCallThisFunction() {
		Context context = Context.enter();
        try {
        	Scriptable scope = context.initStandardObjects();        	
        	context.evaluateString(scope, "function addNumbers(a, b) { return a + b; }", "test", 1, null);        	
        	
        	Function addNumbersFunction = (Function)scope.get("addNumbers", scope);
        	Object result = addNumbersFunction.call(context, scope, null, new Object[] { 1, 2 });
        	assertEquals(3, Context.jsToJava(result, int.class));
        } finally {
        	Context.exit();
        }
	}
}
