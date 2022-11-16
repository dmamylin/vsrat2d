package org.madbunny.vsrat2d.api;

public record FrameContext(
        float deltaTime,
        ApplicationContext app,
        Screen screen,
        KeyboardInput keyboard,
        MouseInput mouse
) {}
