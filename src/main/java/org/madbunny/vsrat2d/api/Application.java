package org.madbunny.vsrat2d.api;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.util.function.Consumer;

public class Application {
    private int windowHeight = 640;
    private int windowWidth = 480;
    private String windowTitle = "";
    private boolean isRunning = false;
    private final Object lock = new Object();

    public Application setWindowHeight(int height) {
        windowHeight = height;
        return this;
    }

    public Application setWindowWidth(int width) {
        windowWidth = width;
        return this;
    }

    public Application setWindowTitle(String title) {
        windowTitle = title;
        return this;
    }

    public void run(Consumer<FrameContext> updater) {
        synchronized (lock) {
            var gameController = new GameController(updater);
            var application = new LwjglApplication(gameController, makeConfig());
            isRunning = true;
            try {
                while (isRunning) {
                    lock.wait();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.gc();
    }

    private LwjglApplicationConfiguration makeConfig() {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        LwjglApplicationConfiguration.disableAudio = true;
        config.width = windowWidth;
        config.height = windowHeight;
        config.title = windowTitle;
        config.resizable = false;
        config.forceExit = false;
        return config;
    }

    private class GameController extends Game {
        private final Consumer<FrameContext> updater;

        public GameController(Consumer<FrameContext> updater) {
            this.updater = updater;
        }

        @Override
        public void create() {
            var screen = new Screen(windowWidth, windowHeight);
            var keyboardProcessor = new KeyboardInputProcessor();
            var mouseProcessor = new MouseInputProcessor(windowWidth, windowHeight);
            Gdx.input.setInputProcessor(new InputMultiplexer(keyboardProcessor, mouseProcessor));
            setScreen(new GameScreen(updater, screen, keyboardProcessor, mouseProcessor));
        }

        @Override
        public void render() {
            var screen = (GameScreen) getScreen();
            if (screen != null) {
                screen.render(Gdx.graphics.getDeltaTime());
            }
        }

        @Override
        public void dispose() {
            super.dispose();
            synchronized (lock) {
                isRunning = false;
                lock.notifyAll();
            }
        }
    }

    private static class GameScreen implements com.badlogic.gdx.Screen {
        private final Consumer<FrameContext> updater;
        private final Screen screen;
        private final KeyboardInputProcessor keyboardProcessor;
        private final MouseInputProcessor mouseProcessor;

        public GameScreen(
                Consumer<FrameContext> updater,
                Screen screen,
                KeyboardInputProcessor keyboardProcessor,
                MouseInputProcessor mouseProcessor
        ) {
            this.updater = updater;
            this.screen = screen;
            this.keyboardProcessor = keyboardProcessor;
            this.mouseProcessor = mouseProcessor;
        }

        @Override
        public void render(float deltaTime) {
            updater.accept(new FrameContext(deltaTime, screen, keyboardProcessor, mouseProcessor));
            keyboardProcessor.reset();
            mouseProcessor.reset();
        }

        @Override
        public void show() {}
        @Override
        public void resize(int w, int h) {}
        @Override
        public void pause() {}
        @Override
        public void resume() {}
        @Override
        public void hide() {}
        @Override
        public void dispose() {}
    }
}
