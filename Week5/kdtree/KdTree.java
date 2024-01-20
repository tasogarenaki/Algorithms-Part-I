/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * A mutable data type KdTree.java that represents a set of points in a two-dimensional space.
 * Implements various operations using a 2D tree structure.
 * Throws an IllegalArgumentException if any argument is null.
 */
public class KdTree {
    private final double xmin, ymin, xmax, ymax;
    private Node root;
    private int num;

    /**
     * Inner class representing a node in the KdTree.
     */
    private static class Node {
        private final Point2D p;
        private final RectHV rect;
        private Node lb;    // the left bottom subtree
        private Node rt;    // the right top subtree

        /**
         * Constructs a node with a point and its corresponding rectangle.
         */
        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
            lb = null;
            rt = null;
        }
    }

    /**
     * Constructs an empty KdTree in the unit square.
     */
    public KdTree() {
        xmin = 0;
        ymin = 0;
        xmax = 1;
        ymax = 1;
        root = null;
        num = 0;
    }

    /**
     * Checks if the KdTree is empty.
     *
     * @return true if the KdTree is empty, false otherwise.
     */
    public boolean isEmpty() {
        return num == 0;
    }

    /**
     * Returns the number of points in the KdTree.
     *
     * @return the number of points in the KdTree.
     */
    public int size() {
        return num;
    }

    /**
     * Compares two points based on their coordinates and orientation.
     *
     * @param p1       the first point.
     * @param p2       the second point.
     * @param vertical true if comparing based on x-coordinate, false for y-coordinate.
     * @return 0 if points are equal, a negative value if p1 < p2, a positive value if p1 > p2.
     */
    private int compareTo(Point2D p1, Point2D p2, boolean vertical) {
        if (p1.equals(p2)) {
            return 0;
        }
        return vertical ? Double.compare(p1.x(), p2.x()) : Double.compare(p1.y(), p2.y());
    }

    /**
     * Inserts a point into the KdTree.
     *
     * @param p the point to be inserted.
     */
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Point is null");
        }

        if (root == null) {
            root = new Node(p, new RectHV(xmin, ymin, xmax, ymax));
            ++num;
            return;
        }

        boolean vertical = true;
        double x1 = xmin, y1 = ymin, x2 = xmax, y2 = ymax;
        Node curr = root;
        Node prev = null;
        int cmp = 0;

        while (curr != null) {
            prev = curr;
            cmp = compareTo(p, curr.p, vertical);
            if (cmp < 0) {
                if (vertical) {
                    x2 = curr.p.x();
                }
                else {
                    y2 = curr.p.y();
                }
                curr = curr.lb;
            }
            else if (cmp > 0) {
                if (vertical) {
                    x1 = curr.p.x();
                }
                else {
                    y1 = curr.p.y();
                }
                curr = curr.rt;
            }
            else {
                return;
            }

            vertical = !vertical;
        }

        ++num;
        Node node = new Node(p, new RectHV(x1, y1, x2, y2));

        if (cmp < 0) {
            prev.lb = node;
        }
        else {
            prev.rt = node;
        }
    }

    /**
     * Checks if the KdTree contains the specified point.
     *
     * @param p the point to check.
     * @return true if the KdTree contains the point, false otherwise.
     */
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Point is null.");
        }

        boolean vertical = true;
        Node curr = root;
        int cmp = 0;
        while (curr != null) {
            cmp = compareTo(p, curr.p, vertical);
            if (cmp < 0) {
                curr = curr.lb;
            }
            else if (cmp > 0) {
                curr = curr.rt;
            }
            else {
                return true;
            }

            vertical = !vertical;
        }

        return false;
    }

    /**
     * Draws the KdTree.
     */
    private void draw(Node node, boolean vertical) {
        if (node == null) {
            return;
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.p.draw();

        if (vertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
        }

        draw(node.lb, !vertical);
        draw(node.rt, !vertical);
    }

    /**
     * Draws the KdTree.
     */
    public void draw() {
        draw(root, true);
    }

    /**
     * Returns all points within the specified rectangle.
     *
     * @param rect the rectangle.
     * @return an iterable of points within the rectangle.
     * @throws IllegalArgumentException if the rectangle is null.
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Rect is null");
        }

        Queue<Point2D> result = new Queue<Point2D>();
        if (root == null) {
            return result;
        }
        Queue<Node> queue = new Queue<Node>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            Node node = queue.dequeue();
            if (rect.contains(node.p)) {
                result.enqueue(node.p);
            }

            if (node.lb != null && rect.intersects(node.lb.rect)) {
                queue.enqueue(node.lb);
            }

            if (node.rt != null && rect.intersects(node.rt.rect)) {
                queue.enqueue(node.rt);
            }
        }

        return result;
    }

    /**
     * Finds the nearest neighbor to a given point in the KdTree.
     *
     * @param node     the current node.
     * @param minPoint the current nearest point.
     * @param p        the point to find the nearest neighbor for.
     * @param vertical true if comparing based on x-coordinate, false for y-coordinate.
     * @return the nearest neighbor point.
     */
    private Point2D nearest(Node node, Point2D minPoint, Point2D p, boolean vertical) {
        if (node == null) {
            return minPoint;
        }

        if (p.distanceSquaredTo(node.p) < p.distanceSquaredTo(minPoint)) {
            minPoint = node.p;
        }

        int cmp = compareTo(p, node.p, vertical);
        if (cmp < 0) {
            minPoint = nearest(node.lb, minPoint, p, !vertical);
            if (node.rt != null && node.rt.rect.distanceSquaredTo(p) < p.distanceSquaredTo(
                    minPoint)) {
                minPoint = nearest(node.rt, minPoint, p, !vertical);
            }
        }
        else if (cmp > 0) {
            minPoint = nearest(node.rt, minPoint, p, !vertical);
            if (node.lb != null && node.lb.rect.distanceSquaredTo(p) < p.distanceSquaredTo(
                    minPoint)) {
                minPoint = nearest(node.lb, minPoint, p, !vertical);
            }
        }

        return minPoint;
    }

    /**
     * Returns the nearest neighbor to a given point in the KdTree.
     *
     * @param p the point to find the nearest neighbor for.
     * @return the nearest neighbor point, or null if the KdTree is empty.
     * @throws IllegalArgumentException if the point is null.
     */
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Point is null");
        }

        if (root == null) {
            return null;
        }

        return nearest(root, root.p, p, true);
    }

    /**
     * Unit testing of the methods (optional).
     *
     * @param args the command-line arguments.
     */
    public static void main(String[] args) {
        KdTree ps = new KdTree();
        ps.insert(new Point2D(0.7, 0.2));
        ps.insert(new Point2D(0.5, 0.4));
        ps.insert(new Point2D(0.2, 0.3));
        ps.insert(new Point2D(0.4, 0.7));
        ps.insert(new Point2D(0.9, 0.6));
        ps.draw();

        StdOut.println(ps.nearest(new Point2D(0.2, 0.2)));
        for (Point2D p : ps.range(new RectHV(0, 0.5, 1, 1))) {
            StdOut.println(p);
        }
    }
}
