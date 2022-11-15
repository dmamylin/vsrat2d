package org.madbunny.vsrat2d.api;

public interface MouseInput {
    boolean isButtonPressed(MouseButton button);
    boolean isButtonClicked(MouseButton button);
    Point2D getMousePosition();
}
