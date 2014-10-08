package me.loki2302;

import math.geom2d.Point2D;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JavaGeomTests {
    private final static double Tolerance = 0.001;

    @Test
    public void canComputeDistanceBetween2Points() {
        Point2D p1 = new Point2D(-1, 0);
        Point2D p2 = new Point2D(1, 0);
        assertEquals(2, p1.distance(p2), Tolerance);
    }
}
