package org.madbunny.vsrat2d.api;

public record Point2D(float x, float y) {
    public Point2D negate() {
        return new Point2D(-x, -y);
    }

    public Point2D add(Point2D other) {
        return Point2D.add(this, other);
    }

    public Point2D subtract(Point2D other) {
        return Point2D.subtract(this, other);
    }

    public Point2D multiply(float scalar) {
        return Point2D.multiply(this, scalar);
    }

    public Point2D divide(float scalar) {
        return Point2D.divide(this, scalar);
    }

    public float norm() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public static Point2D negate(Point2D v) {
        return v.negate();
    }

    public static Point2D add(Point2D lhs, Point2D rhs) {
        return new Point2D(lhs.x() + rhs.x(), lhs.y() + rhs.y());
    }

    public static Point2D subtract(Point2D lhs, Point2D rhs) {
        return new Point2D(lhs.x - rhs.x, lhs.y - rhs.y);
    }

    public static Point2D multiply(Point2D p, float s) {
        return new Point2D(p.x * s, p.y * s);
    }

    public static Point2D divide(Point2D p, float s) {
        return new Point2D(p.x / s, p.y / s);
    }
}
