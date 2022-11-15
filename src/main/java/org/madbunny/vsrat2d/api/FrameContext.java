package org.madbunny.vsrat2d.api;

public record FrameContext(
        float deltaTime,
        Screen screen,
        KeyboardInput keyboard,
        MouseInput mouse
) {}
