package me.loki2302;

import georegression.metric.Intersection2D_F64;
import georegression.struct.line.LineSegment2D_F64;
import georegression.struct.point.Point2D_F64;

public class App {
    public static void main(String[] args) {

        LineSegment2D_F64 line1 = new LineSegment2D_F64(
                new Point2D_F64(-1, 0),
                new Point2D_F64(1, 0));
        LineSegment2D_F64 line2 = new LineSegment2D_F64(
                new Point2D_F64(0, -1),
                new Point2D_F64(0, 1));

        Point2D_F64 intersection = Intersection2D_F64.intersection(line1, line2, null);
        System.out.println(intersection);
    }
}
