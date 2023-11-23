/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final LineSegment[] lineSegments;

    /**
     * finds all line segments containing 4 points
     *
     * @param points an array of points to find line segments from
     * @throws IllegalArgumentException if the input array is null, contains null points,
     *                                  or has duplicate points
     */
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Input is null.");
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Null point in the array.");
            }
        }

        Point[] localPoints = points.clone();
        Arrays.sort(localPoints);

        for (int i = 1; i < localPoints.length; i++) {
            if (localPoints[i].compareTo(localPoints[i - 1]) == 0) {
                throw new IllegalArgumentException("Input contains duplicate points");
            }
        }

        ArrayList<LineSegment> res = new ArrayList<LineSegment>();
        if (localPoints.length > 3) {
            for (int i = 0; i < localPoints.length - 3; i++) {
                for (int j = i + 1; j < localPoints.length - 2; j++) {
                    for (int p = j + 1; p < localPoints.length - 1; p++) {
                        for (int k = p + 1; k < localPoints.length; k++) {
                            Point[] temp = {
                                    localPoints[i], localPoints[j], localPoints[p], localPoints[k]
                            };
                            if (isCollinear(temp)) {
                                res.add(getSegment(temp));
                            }
                        }
                    }
                }
            }
        }
        lineSegments = res.toArray(new LineSegment[res.size()]);
    }

    /**
     * the number of line segments
     *
     * @return the number of line segments
     */
    public int numberOfSegments() {
        return lineSegments.length;
    }

    /**
     * the line segments
     *
     * @return an array of LineSegment objects
     */
    public LineSegment[] segments() {
        return lineSegments.clone();
    }

    /**
     * Checks if four given points are collinear.
     *
     * @param points an array of four points to be checked for collinearity
     * @return true if the slopes between the first point and the other three points are equal,
     * indicating collinearity
     */
    private boolean isCollinear(Point[] points) {
        double firstSlope = points[0].slopeTo(points[1]);
        double secondSlope = points[0].slopeTo(points[2]);
        double thirdSlope = points[0].slopeTo(points[3]);

        return firstSlope == secondSlope && firstSlope == thirdSlope;
    }

    /**
     * Constructs a LineSegment from an array of four points.
     *
     * @param points an array of four points
     * @return a LineSegment formed by the two endpoints of the sorted array
     */
    private LineSegment getSegment(Point[] points) {
        Arrays.sort(points);
        return new LineSegment(points[0], points[3]);
    }


    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
