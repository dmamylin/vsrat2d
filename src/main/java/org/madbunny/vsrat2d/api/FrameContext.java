package org.madbunny.vsrat2d.api;

public record FrameContext(
        /*
          Stores elapsed time in seconds since previous frame
         */
        float deltaTime,

        /*
          Stores elapsed time in seconds since application start
         */
        double currentTime,

        ApplicationContext app,
        Screen screen,
        KeyboardInput keyboard,
        MouseInput mouse
) {}
