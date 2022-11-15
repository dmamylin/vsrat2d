package org.madbunny.vsrat2d.api;

public record Point2D(float x, float y) {
    public float norm() {
        return (float) Math.sqrt(x * x + y * y);
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
