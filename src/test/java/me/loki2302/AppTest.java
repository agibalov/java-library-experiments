package me.loki2302;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AppTest {
    @Test
    public void canGetAllMethods() throws IOException, ParseException {
        CompilationUnit compilationUnit = JavaParser.parse(new File("src/main/java/me/loki2302/Calculator.java"));
        GetAllMethodsVisitor getAllMethodsVisitor = new GetAllMethodsVisitor();
        getAllMethodsVisitor.visit(compilationUnit, null);

        List<String> methods = getAllMethodsVisitor.getMethodNames();
        assertEquals(2, methods.size());
        assertTrue(methods.contains("addNumbers"));
        assertTrue(methods.contains("subNumbers"));
    }

    private static class GetAllMethodsVisitor extends VoidVisitorAdapter<Object> {
        private final List<String> methodNames = new ArrayList<>();

        @Override
        public void visit(MethodDeclaration n, Object arg) {
            methodNames.add(n.getName());
        }

        public List<String> getMethodNames() {
            return methodNames;
        }
    }
}
