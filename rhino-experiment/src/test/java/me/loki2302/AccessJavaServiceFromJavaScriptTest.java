package me.loki2302;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class AccessJavaServiceFromJavaScriptTest {
    @Test
    public void canAccessPureJavaService() {
        Context context = Context.enter();
        try {
            Scriptable scope = context.initStandardObjects();

            MyService myService = new MyService();
            Object jsMyService = Context.javaToJS(myService, scope);
            ScriptableObject.putProperty(scope, "my", jsMyService);

            Object result = context.evaluateString(scope, "my.twice(1) + my.twice(2)", "test", 1, null);
            assertEquals(6, Context.jsToJava(result, int.class));
        } finally {
            Context.exit();
        }
    }

    public static class MyService {
        public int twice(int x) {
            return x * 2;
        }
    }
}
