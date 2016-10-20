package me.loki2302;

import me.loki2302.dummy.Calculator;
import org.junit.Before;
import org.junit.Test;
import spoon.Launcher;
import spoon.SpoonAPI;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtComment;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DummyTest {
    private SpoonAPI spoon;

    @Before
    public void initSpoon() {
        spoon = new Launcher();
        spoon.addInputResource("src/test/java/me/loki2302/dummy");
        spoon.getEnvironment().setCommentEnabled(true);
        spoon.buildModel();
    }

    @Test
    public void canGetAllClasses() {
        List<CtType<?>> classes = spoon.getFactory().Class().getAll();
        assertEquals(1, classes.size());
        CtType<?> theOnlyClass = classes.get(0);
        assertEquals(Calculator.class.getName(), theOnlyClass.getQualifiedName());
    }

    @Test
    public void canGetAllClassMethods() {
        CtClass<?> calculatorClass = spoon.getFactory().Class().get("me.loki2302.dummy.Calculator");
        assertEquals(1, calculatorClass.getMethods().size());
        assertTrue(calculatorClass.getMethods().stream().anyMatch(m -> m.getSimpleName().equals("add")));
    }

    @Test
    public void canGetMethodContents() {
        CtTypeReference<?> intTypeReference = spoon.getFactory().Code().createCtTypeReference(int.class);

        CtMethod<?> addMethod = spoon.getFactory().Class()
                .get("me.loki2302.dummy.Calculator")
                .getMethod(intTypeReference, "add", intTypeReference, intTypeReference);

        List<CtParameter<?>> parameters = addMethod.getParameters();
        assertEquals(2, parameters.size());
        assertEquals(intTypeReference, parameters.get(0).getType());
        assertEquals("x", parameters.get(0).getSimpleName());

        CtBlock<?> body = addMethod.getBody();
        List<CtStatement> statements = body.getStatements();
        assertEquals(1, statements.size());

        CtStatement theOnlyStatement = statements.get(0);
        assertTrue(theOnlyStatement instanceof CtReturn);

        CtReturn<?> returnStatement = (CtReturn<?>)theOnlyStatement;
        assertEquals("x + y", returnStatement.getReturnedExpression().toString());
    }

    @Test
    public void canGetMethodComments() {
        CtTypeReference<?> intTypeReference = spoon.getFactory().Code().createCtTypeReference(int.class);

        CtMethod<?> addMethod = spoon.getFactory().Class()
                .get("me.loki2302.dummy.Calculator")
                .getMethod(intTypeReference, "add", intTypeReference, intTypeReference);

        if(true) {
            CtParameter<?> xParameter = addMethod.getParameters().get(0);
            assertEquals(1, xParameter.getComments().size());
            assertEquals("some parameter comment here", xParameter.getComments().get(0).getContent());
        }

        if(true) {
            List<CtComment> comments = addMethod.getComments();
            assertEquals(1, comments.size());
            assertEquals("Some method comment here", comments.get(0).getContent());
        }

        if(true) {
            List<CtComment> comments = addMethod.getBody().getComments();
            assertEquals(0, comments.size());
        }

        if(true) {
            List<CtComment> comments = addMethod.getBody().getStatement(0).getComments();
            assertEquals(3, comments.size());
            assertEquals("just return a sum", comments.get(0).getContent());
            assertEquals("of x and y", comments.get(1).getContent());
            assertEquals("some return comment here", comments.get(2).getContent());
        }
    }
}
