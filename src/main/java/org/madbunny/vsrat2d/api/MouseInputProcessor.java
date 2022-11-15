package org.madbunny.vsrat2d.api;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import java.util.Arrays;

class MouseInputProcessor extends InputAdapter implements MouseInput {
    private static final int[] gdxButtonToLocal = makeGdxButtonMap();

    private final boolean[] isPressed = new boolean[MouseButton.values().length];
    private final boolean[] isClicked = new boolean[MouseButton.values().length];
    private final int windowWidth;
    private final int windowHeight;

    private Point2D mousePosition;

    MouseInputProcessor(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        mousePosition = new Point2D(windowWidth / 2.0f, windowHeight / 2.0f);
    }

    @Override
    public boolean isButtonPressed(MouseButton button) {
        if (MouseButton.UNKNOWN.equals(button)) {
            return false;
        }
        return isPressed[button.value];
    }

    @Override
    public boolean isButtonClicked(MouseButton button) {
        if (MouseButton.UNKNOWN.equals(button)) {
            return false;
        }
        return isClicked[button.value];
    }

    @Override
    public Point2D getMousePosition() {
        return mousePosition;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        var buttonId = gdxButtonToLocal[button];
        if (MouseButton.UNKNOWN.value == buttonId) {
            return false;
        }
        isPressed[buttonId] = true;
        mouseMoved(x, y);
        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        var buttonId = gdxButtonToLocal[button];
        if (MouseButton.UNKNOWN.value == buttonId) {
            return false;
        }
        if (isPressed[buttonId]) {
            isClicked[buttonId] = true;
            isPressed[buttonId] = false;
        }
        mouseMoved(x, y);
        return true;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        mouseMoved(x, y);
        return true;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        x = Math.min(Math.max(0, x), windowWidth - 1);
        y = windowHeight - Math.min(Math.max(0, y), windowHeight - 1) - 1;
        mousePosition = new Point2D(x, y);
        return true;
    }

    void reset() {
        Arrays.fill(isClicked, false);
    }

    private static int[] makeGdxButtonMap() {
        var result = new int[MouseButton.values().length];
        Arrays.fill(result, MouseButton.UNKNOWN.value);
        for (var button : MouseButton.values()) {
            var gdxKeycode = getGdxButtonCode(button);
            if (gdxKeycode >= 0) {
                result[gdxKeycode] = button.value;
            }
        }
        return result;
    }

    private static int getGdxButtonCode(MouseButton button) {
        switch (button) {
            case LEFT -> { return Input.Buttons.LEFT; }
            case MIDDLE -> { return Input.Buttons.MIDDLE; }
            case RIGHT -> { return Input.Buttons.RIGHT; }
        }
        return -1;
    }
}
