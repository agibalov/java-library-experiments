package me.loki2302;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class AngularJSTest {
    @Test
    public void canUseAngularsInjectorToGetServiceInstance() throws IOException {
        Context context = Context.enter();
        context.setOptimizationLevel(-1); // disable bytecode generation because of large code
        try {
            Scriptable scope = context.initStandardObjects();
            
            loadResource(context, scope, "env-fix.js");
            loadResource(context, scope, "env.rhino.1.2.js");
            loadResource(context, scope, "angular.min.js");
            loadResource(context, scope, "angular-app.js");
            
            Scriptable testService = (Scriptable)context.evaluateString(scope, "injector.get('testService')", "test", 1, null);
            Function helloWorldFunc = (Function)ScriptableObject.getProperty(testService, "helloWorld");
            Object result = helloWorldFunc.call(context, scope, testService, new Object[]{});
            assertEquals("hello world!", Context.jsToJava(result, String.class));
        } finally {
            Context.exit();
        }
    }
    
    private static void loadResource(Context context, Scriptable scope, String resourceName) throws IOException {
        URL resourceUrl = Resources.getResource(resourceName);
        String resourceContents = Resources.toString(resourceUrl, Charsets.UTF_8);
        context.evaluateString(scope, resourceContents, resourceName, 1, null);
    }
}
