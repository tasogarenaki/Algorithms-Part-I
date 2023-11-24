/**
 * FastCollinearPoints
 * <p>
 * Implements a sorting-based solution to find all line segments containing 4 or more collinear
 * points.
 * The algorithm is more efficient than the brute-force solution by leveraging the following
 * approach:
 * <p>
 * - Treat each point `p` as the origin.
 * - For each other point `q`, calculate the slope it makes with `p`.
 * - Sort the points based on the slopes they make with `p`.
 * - Check if any 3 or more adjacent points in the sorted order have equal slopes with respect to
 * `p`.
 * If so, these points, together with `p`, are collinear.
 * <p>
 * Applying this method for each of the `n` points yields an efficient algorithm to solve the
 * problem. The
 * sorting operation is the bottleneck, but it brings together points with equal slopes, making the
 * algorithm
 * faster than the brute-force solution.
 *
 * @author Terry
 */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {
    private final LineSegment[] lineSegments;

    /**
     * Finds all line segments containing 4 or more points.
     *
     * @param points an array of points to find line segments from
     * @throws IllegalArgumentException if the input array is null, contains null points,
     *                                  or has duplicate points
     */
    public FastCollinearPoints(Point[] points) {
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
            Point[] sortedPoints = points.clone();
            for (Point p : localPoints) {
                Arrays.sort(sortedPoints, p.slopeOrder());
                findSegments(sortedPoints, p, res);
            }
        }
        lineSegments = res.toArray(new LineSegment[res.size()]);
    }

    /**
     * Returns the number of line segments.
     */
    public int numberOfSegments() {
        return lineSegments.length;
    }

    /**
     * Returns an array of LineSegment objects representing the line segments.
     */
    public LineSegment[] segments() {
        return lineSegments.clone();
    }

    /**
     * Finds collinear segments with respect to a reference point.
     *
     * @param points         an array of points to find collinear segments from
     * @param referencePoint the reference point to consider as the origin
     * @param result         the ArrayList to store identified LineSegments
     */
    private void findSegments(Point[] points, Point referencePoint,
                              ArrayList<LineSegment> result) {
        // begin from index 1 to skip the reference point p itself.
        int start = 1;
        double slope = referencePoint.slopeTo(points[start]);

        // Iterate through points to identify collinear segments
        for (int i = start + 1; i < points.length; i++) {
            double tempSlope = referencePoint.slopeTo(points[i]);
            if (!areSlopesCollinear(tempSlope, slope)) {
                processSegment(points, referencePoint, start, i, result);
                start = i;
                slope = tempSlope;
            }
        }
        // Process the last set of collinear points
        processSegment(points, referencePoint, start, points.length, result);
    }

    /**
     * Checks if two slopes are collinear.
     *
     * @param firstSlope  the slope to compare
     * @param secondSlope the slope to compare against
     * @return true if the slopes are collinear, false otherwise
     */
    private boolean areSlopesCollinear(double firstSlope, double secondSlope) {
        return Double.compare(firstSlope, secondSlope) == 0;
    }

    /**
     * Processes a collinear segment and adds it to the result list if it contains 4 or more points.
     *
     * @param points         an array of points within the segment
     * @param referencePoint the reference point used for sorting
     * @param start          the starting index of the segment
     * @param end            the ending index of the segment
     * @param result         the ArrayList to store identified LineSegments
     */
    private void processSegment(Point[] points, Point referencePoint, int start, int end,
                                ArrayList<LineSegment> result) {
        if (end - start >= 3) {
            Point[] segment = generateSegment(points, referencePoint, start, end);
            // Avoid duplicates by checking the starting point of the segment
            if (segment[0].compareTo(referencePoint) == 0) {
                result.add(new LineSegment(segment[0], segment[1]));
            }
        }
    }

    /**
     * Generates a LineSegment from a subset of points within a specified range.
     *
     * @param points         an array of points within the segment
     * @param referencePoint the reference point used for sorting
     * @param start          the starting index of the segment
     * @param end            the ending index of the segment
     * @return a LineSegment formed by the two endpoints of the sorted array
     */
    private Point[] generateSegment(Point[] points, Point referencePoint, int start, int end) {
        ArrayList<Point> temp = new ArrayList<>();
        temp.add(referencePoint);

        for (int i = start; i < end; i++) {
            temp.add(points[i]);
        }

        // Sort the points to determine the endpoints of the segment
        temp.sort(Comparator.naturalOrder());
        return new Point[] { temp.get(0), temp.get(temp.size() - 1) };
    }

    /**
     * Main method for testing the FastCollinearPoints class.
     *
     * @param args the command-line arguments
     */
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
