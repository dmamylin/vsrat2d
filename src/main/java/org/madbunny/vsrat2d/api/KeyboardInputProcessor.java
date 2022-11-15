package org.madbunny.vsrat2d.api;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import java.util.Arrays;

class KeyboardInputProcessor extends InputAdapter implements KeyboardInput {
    private static final int KEYBOARD_KEYCODE_COUNT = 1 << 8;
    private static final int BITMASK_BLOCK_SIZE = 1 << 6;
    private static final int KEYBOARD_BITMASK_SIZE = KEYBOARD_KEYCODE_COUNT / BITMASK_BLOCK_SIZE;

    private static final int[] gdxKeycodeToLocal = makeGdxKeycodeMap();

    private final long[] isPressed = new long[KEYBOARD_BITMASK_SIZE];
    private final long[] isClicked = new long[KEYBOARD_BITMASK_SIZE];

    @Override
    public boolean isKeyPressed(KeyboardKey key) {
        if (KeyboardKey.UNKNOWN.equals(key)) {
            return false;
        }
        return hasBit(isPressed, key.value - 1);
    }

    @Override
    public boolean isKeyClicked(KeyboardKey key) {
        if (KeyboardKey.UNKNOWN.equals(key)) {
            return false;
        }
        return hasBit(isClicked, key.value - 1);
    }

    @Override
    public boolean keyDown(int keycode) {
        keycode = gdxKeycodeToLocal[keycode];
        if (keycode == KeyboardKey.UNKNOWN.value) {
            return false;
        }
        setBit(isPressed, keycode - 1);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        keycode = gdxKeycodeToLocal[keycode];
        if (keycode == KeyboardKey.UNKNOWN.value) {
            return false;
        }
        keycode--;
        if (hasBit(isPressed, keycode)) {
            setBit(isClicked, keycode);
            unsetBit(isPressed, keycode);
        }
        return true;
    }

    void reset() {
        resetBitMask(isClicked);
    }

    private static void resetBitMask(long[] mask) {
        Arrays.fill(mask, 0);
    }

    private static boolean hasBit(long[] mask, int i) {
        int blockId = i / 64;
        int bitId = i % 64;
        long bit = mask[blockId] & (1L << bitId);
        return bit != 0;
    }

    private static void setBit(long[] mask, int i) {
        int blockId = i / 64;
        int bitId = i % 64;
        mask[blockId] = mask[blockId] | (1L << bitId);
    }

    private static void unsetBit(long[] mask, int i) {
        int blockId = i / 64;
        int bitId = i % 64;
        long bitMask = ~(1L << bitId);
        mask[blockId] = mask[blockId] & bitMask;
    }

    private static int[] makeGdxKeycodeMap() {
        var result = new int[Input.Keys.MAX_KEYCODE + 1];
        Arrays.fill(result, KeyboardKey.UNKNOWN.value);
        for (var key : KeyboardKey.values()) {
            var gdxKeycode = Input.Keys.valueOf(key.name());
            if (gdxKeycode > 0) {
                result[gdxKeycode] = key.value;
            }
        }
        return result;
    }
}
