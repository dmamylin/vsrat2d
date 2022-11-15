package org.madbunny.vsrat2d.api;

public enum MouseButton {
    UNKNOWN(0),
    LEFT(1),
    RIGHT(2),
    MIDDLE(3);

    public final int value;

    MouseButton(int value) {
        this.value = value;
    }
}
