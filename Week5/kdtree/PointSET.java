/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

/**
 * A mutable data type PointSET.java that represents a set of points in the unit square.
 * Implements the following API using a red–black BST.
 * Throws an IllegalArgumentException if any argument is null.
 */
public class PointSET {
    private final SET<Point2D> set;

    /**
     * Constructs an empty set of points.
     */
    public PointSET() {
        set = new SET<Point2D>();
    }

    /**
     * Checks if the set is empty.
     *
     * @return true if the set is empty, false otherwise.
     */
    public boolean isEmpty() {
        return set.isEmpty();
    }

    /**
     * Returns the number of points in the set.
     *
     * @return the number of points in the set.
     */
    public int size() {
        return set.size();
    }

    /**
     * Adds the point to the set (if it is not already in the set).
     *
     * @param p the point to be added.
     */
    public void insert(Point2D p) {
        set.add(p);
    }

    /**
     * Checks if the set contains the specified point.
     *
     * @param p the point to check.
     * @return true if the set contains the point, false otherwise.
     */
    public boolean contains(Point2D p) {
        return set.contains(p);
    }

    /**
     * Draws all points to standard draw.
     */
    public void draw() {
        for (Point2D p : set) {
            p.draw();
        }
    }

    /**
     * Returns all points that are inside the specified rectangle (or on the boundary).
     *
     * @param rect the rectangle.
     * @return an iterable of points inside the rectangle.
     * @throws IllegalArgumentException if the rectangle is null.
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Rect is null.");
        }

        Queue<Point2D> queue = new Queue<Point2D>();
        for (Point2D p : set) {
            if (rect.contains(p)) {
                queue.enqueue(p);
            }
        }

        return queue;
    }

    /**
     * Returns the nearest neighbor in the set to the specified point.
     *
     * @param p the point.
     * @return the nearest neighbor in the set to the point, or null if the set is empty.
     * @throws IllegalArgumentException if the point is null.
     */
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Point is null.");
        }

        Point2D res = null;
        double minDist = Double.MAX_VALUE;
        for (Point2D point : set) {
            double dist = point.distanceSquaredTo(p);
            if (dist < minDist) {
                minDist = dist;
                res = point;
            }
        }

        return res;
    }

    /**
     * Unit testing of the methods (optional).
     *
     * @param args the command-line arguments.
     */
    public static void main(String[] args) {
        PointSET ps = new PointSET();
        ps.insert(new Point2D(0, 0));
        ps.insert(new Point2D(0, 1));
        ps.insert(new Point2D(1, 0));
        ps.insert(new Point2D(1, 1));
        ps.draw();

        StdOut.println(ps.nearest(new Point2D(0.2, 0.2)));
        for (Point2D p : ps.range(new RectHV(0, 0.5, 1, 1))) {
            StdOut.println(p);
        }
    }
}
