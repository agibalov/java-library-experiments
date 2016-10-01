package me.loki2302;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.apache.tinkerpop.gremlin.util.iterator.IteratorUtils;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class DummyTest {
    @Test
    public void canGetAllVertices() {
        Graph graph = makeGraph(new File("./src/test/java/me/loki2302/DummyTest.java"));

        GraphTraversal<Vertex, Vertex> traversal = graph.traversal().V();
        List<Vertex> vertices = IteratorUtils.list(traversal);
        assertTrue(vertices.stream().anyMatch(v -> v.label().contains("Calculator")));
        assertTrue(vertices.stream().anyMatch(v -> v.label().contains("Adder")));
        assertTrue(vertices.stream().anyMatch(v -> v.label().contains("Subtractor")));
        assertTrue(vertices.stream().anyMatch(v -> v.label().contains("Negator")));
    }

    @Test
    public void canGetAllConnections() {
        Graph graph = makeGraph(new File("./src/test/java/me/loki2302/DummyTest.java"));

        GraphTraversal<Edge, Edge> traversal = graph.traversal().E().hasLabel("uses");
        List<Edge> edges = IteratorUtils.list(traversal);

        assertTrue(edges.stream().anyMatch(e ->
                e.outVertex().label().contains("Calculator") &&
                e.inVertex().label().contains("Adder") &&
                e.<String>value("as").contains("adder")));

        assertTrue(edges.stream().anyMatch(e ->
                e.outVertex().label().contains("Calculator") &&
                e.inVertex().label().contains("Subtractor") &&
                e.<String>value("as").contains("subtractor")));

        assertTrue(edges.stream().anyMatch(e ->
                e.outVertex().label().contains("Subtractor") &&
                e.inVertex().label().contains("Adder") &&
                e.<String>value("as").contains("adder")));

        assertTrue(edges.stream().anyMatch(e ->
                e.outVertex().label().contains("Subtractor") &&
                e.inVertex().label().contains("Negator") &&
                e.<String>value("as").contains("negator")));
    }

    private static Graph makeGraph(File sourceCode) {
        Graph graph = TinkerGraph.open();

        JavaProjectBuilder javaProjectBuilder = new JavaProjectBuilder();
        javaProjectBuilder.addSourceTree(sourceCode);

        for (JavaClass javaClass : javaProjectBuilder.getClasses()) {
            String className = javaClass.getFullyQualifiedName();
            graph.addVertex(className);
        }

        for (JavaClass javaClass : javaProjectBuilder.getClasses()) {
            String className = javaClass.getFullyQualifiedName();
            Vertex sourceClassVertex = graph.traversal().V().hasLabel(className).next();

            for (JavaField field : javaClass.getFields()) {
                JavaClass fieldClass = field.getType();
                String fieldName = field.getName();

                Vertex targetClassVertex = graph.traversal().V().hasLabel(fieldClass.getFullyQualifiedName()).next();
                sourceClassVertex.addEdge("uses", targetClassVertex, "as", fieldName);
            }
        }

        return graph;
    }

    public static class Calculator {
        private final Adder adder;
        private final Subtractor subtractor;

        public Calculator(Adder adder, Subtractor subtractor) {
            this.adder = adder;
            this.subtractor = subtractor;
        }

        public int add(int x, int y) {
            return adder.add(x, y);
        }

        public int sub(int x, int y) {
            return subtractor.subtract(x, y);
        }
    }

    public static class Adder {
        public int add(int x, int y) {
            return x + y;
        }
    }

    public static class Subtractor {
        private final Adder adder;
        private final Negator negator;

        public Subtractor(Adder adder, Negator negator) {
            this.adder = adder;
            this.negator = negator;
        }

        public int subtract(int x, int y) {
            int minusY = negator.negate(y);
            return adder.add(x, minusY);
        }
    }

    public static class Negator {
        public int negate(int x) {
            return -x;
        }
    }
}
