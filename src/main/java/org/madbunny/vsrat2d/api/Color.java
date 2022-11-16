package org.madbunny.vsrat2d.api;

public record Color(float red, float green, float blue, float alpha) {
    private static final float DEFAULT_ALPHA = 1;

    public Color(float red, float green, float blue) {
        this(red, green, blue, DEFAULT_ALPHA);
    }
}
