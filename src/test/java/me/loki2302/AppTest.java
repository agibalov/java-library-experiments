package me.loki2302;

import org.jgrapht.DirectedGraph;
import org.jgrapht.ext.DOTImporter;
import org.jgrapht.ext.ImportException;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AppTest {
    @Test
    public void canGetAllVerticesAndEdges() throws ImportException {
        DirectedGraph<String, DefaultEdge> g
                = new DefaultDirectedGraph<>(DefaultEdge.class);

        DOTImporter<String, DefaultEdge> importer = new DOTImporter<>(
                (label, attributes) -> label,
                (from, to, label, attributes) -> new DefaultEdge());

        importer.read(String.join("\n",
                "digraph G {",
                " a -> b;",
                " b -> c;",
                " c -> a;",
                "}"), (AbstractBaseGraph<String, DefaultEdge>)g);

        Set<String> vertexSet = g.vertexSet();
        assertEquals(3, vertexSet.size());
        assertTrue(vertexSet.contains("a"));
        assertTrue(vertexSet.contains("b"));
        assertTrue(vertexSet.contains("c"));

        Set<DefaultEdge> edgeSet = g.edgeSet();
        assertEquals(3, edgeSet.size());
        assertTrue(edgeSet.stream().anyMatch(e -> g.getEdgeSource(e).equals("a") && g.getEdgeTarget(e).equals("b")));
        assertTrue(edgeSet.stream().anyMatch(e -> g.getEdgeSource(e).equals("b") && g.getEdgeTarget(e).equals("c")));
        assertTrue(edgeSet.stream().anyMatch(e -> g.getEdgeSource(e).equals("c") && g.getEdgeTarget(e).equals("a")));
    }
}
