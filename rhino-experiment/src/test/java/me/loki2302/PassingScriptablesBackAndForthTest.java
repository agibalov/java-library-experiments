package me.loki2302;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSGetter;
import org.mozilla.javascript.annotations.JSSetter;

public class PassingScriptablesBackAndForthTest {
    @Test
    public void canPassScriptableObjectToJavaScript() 
            throws IllegalAccessException, InstantiationException, InvocationTargetException {

        Context context = Context.enter();
        try {
            Scriptable scope = context.initStandardObjects();
            ScriptableObject.defineClass(scope, Person.class);

            String script = 
                    "function getId(person) { return person.id; }; " + 
                    "function getName(person) { return person.name; }; " + 
                    "function changeName(person) { person.name = 'xxx'; }; ";

            context.evaluateString(scope, script, "test", 1, null);

            Function getId = (Function) scope.get("getId", scope);
            Function getName = (Function) scope.get("getName", scope);
            Function changeName = (Function) scope.get("changeName", scope);

            Scriptable scriptablePerson = context.newObject(scope, "Person");
            ScriptableObject.putProperty(scriptablePerson, "id", 123);
            ScriptableObject.putProperty(scriptablePerson, "name", "loki2302");

            Object objId = getId.call(context, scope, null, new Object[] { scriptablePerson });
            assertEquals(123, Context.jsToJava(objId, int.class));

            Object objName = getName.call(context, scope, null, new Object[] { scriptablePerson });
            assertEquals("loki2302", Context.jsToJava(objName, String.class));

            changeName.call(context, scope, null, new Object[] { scriptablePerson });
            Object objChangedName = ScriptableObject.getProperty(scriptablePerson, "name");
            assertEquals("xxx", Context.jsToJava(objChangedName, String.class));
        } finally {
            Context.exit();
        }
    }

    @Test
    public void canGetScriptableJavaObjectFromJavaScript() 
            throws IllegalAccessException, InstantiationException, InvocationTargetException {

        Context context = Context.enter();
        try {
            Scriptable scope = context.initStandardObjects();
            ScriptableObject.defineClass(scope, Person.class);

            String script = "function makePerson() { return new Person(123, 'loki2302'); };";

            context.evaluateString(scope, script, "test", 1, null);

            Function makePerson = (Function) scope.get("makePerson", scope);
            Object result = makePerson.call(context, scope, null, new Object[] {});
            Person person = (Person) Context.jsToJava(result, Person.class);
            assertEquals(123, person.id);
            assertEquals("loki2302", person.name);
        } finally {
            Context.exit();
        }
    }

    public static class Person extends ScriptableObject {
        private static final long serialVersionUID = -7159973157255302356L;

        private int id;
        private String name;

        public Person() {
        }

        @JSConstructor
        public Person(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @JSGetter
        public int getId() {
            return id;
        }

        @JSSetter
        public void setId(int id) {
            this.id = id;
        }

        @JSGetter
        public String getName() {
            return name;
        }

        @JSSetter
        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String getClassName() {
            return "Person";
        }
    }
}
