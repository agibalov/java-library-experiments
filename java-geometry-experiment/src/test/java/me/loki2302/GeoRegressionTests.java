package me.loki2302;

import georegression.metric.Distance2D_F64;
import georegression.metric.Intersection2D_F64;
import georegression.struct.line.LineParametric2D_F64;
import georegression.struct.line.LineSegment2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Vector2D_F64;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GeoRegressionTests {
    private final static double Tolerance = 0.001;

    @Test
    public void canComputeDistanceBetween2Points() {
        Point2D_F64 p1 = new Point2D_F64(-1, 0);
        Point2D_F64 p2 = new Point2D_F64(1, 0);
        assertEquals(2, p1.distance(p2), Tolerance);
    }

    @Test
    public void canComputeDistanceBetweenLineAndPoint() {
        Point2D_F64 p = new Point2D_F64(0, 1);
        LineParametric2D_F64 line = new LineParametric2D_F64(
                new Point2D_F64(0, 0),
                new Vector2D_F64(1, 0));

        assertEquals(1, Distance2D_F64.distance(line, p), Tolerance);
    }

    @Test
    public void canIntersect2LineSegments() {
        LineSegment2D_F64 line1 = new LineSegment2D_F64(
                new Point2D_F64(-1, 0),
                new Point2D_F64(1, 0));
        LineSegment2D_F64 line2 = new LineSegment2D_F64(
                new Point2D_F64(0, -1),
                new Point2D_F64(0, 1));

        Point2D_F64 point = Intersection2D_F64.intersection(line1, line2, null);
        assertEquals(0, point.getX(), Tolerance);
        assertEquals(0, point.getY(), Tolerance);
    }

    @Test
    public void canIntersectLineAndALineSegment() {
        LineSegment2D_F64 line1 = new LineSegment2D_F64(
                new Point2D_F64(-1, 0),
                new Point2D_F64(1, 0));
        LineParametric2D_F64 line2 = new LineParametric2D_F64(
                new Point2D_F64(0, 0),
                new Vector2D_F64(0, 1));

        double distance = Intersection2D_F64.intersection(line2, line1);
        Point2D_F64 point = line2.getPointOnLine(distance);
        assertEquals(0, point.getX(), Tolerance);
        assertEquals(0, point.getY(), Tolerance);
    }
}
