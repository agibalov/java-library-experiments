package me.loki2302;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import spoon.Launcher;
import spoon.SpoonAPI;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;

import static org.junit.Assert.assertEquals;

public class CommentsTest {
    @Rule
    public TestComment testComment = new TestComment("src/test/java/me/loki2302");

    /**
     * I am
     * the test
     */
    @Test
    public void canGetMyOwnComments() {
        String description = testComment.getDescription();
        assertEquals("I am\nthe test", description);
    }

    public static class TestComment extends TestWatcher {
        private final String codeRoot;

        private SpoonAPI spoonAPI;
        private String className;
        private String methodName;

        public TestComment(String codeRoot) {
            this.codeRoot = codeRoot;
        }

        @Override
        protected void starting(Description description) {
            spoonAPI = new Launcher();
            spoonAPI.addInputResource(codeRoot);
            spoonAPI.getEnvironment().setCommentEnabled(true);
            spoonAPI.buildModel();

            className = description.getClassName();
            methodName = description.getMethodName();
        }

        public String getDescription() {
            CtClass<?> testClass = spoonAPI.getFactory().Class().get(className);
            CtMethod<?> testMethod = testClass.getMethod(methodName);
            String testComment = testMethod.getDocComment();
            return testComment;
        }
    }
}
