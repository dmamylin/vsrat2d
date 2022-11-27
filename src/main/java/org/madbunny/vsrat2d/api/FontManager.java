package org.madbunny.vsrat2d.api;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;
import java.util.Map;

public class FontManager {
    private int nextFontId = 0;
    private final Map<Integer, BitmapFont> fonts = new HashMap<>();

    public int createFont(String path, int size) {
        var generator = new FreeTypeFontGenerator(Gdx.files.internal(path));
        var fontParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParams.size = size;
        var font = generator.generateFont(fontParams);
        fonts.put(nextFontId, font);
        generator.dispose();
        return nextFontId++;
    }

    public void removeFont(int fontId) {
        var old = fonts.remove(fontId);
        if (old != null) {
            old.dispose();
        }
    }

    public Size2D getTextDimensions(int fontId, String text) {
        var layout = new GlyphLayout(fonts.get(fontId), text);
        return new Size2D(layout.width, layout.height);
    }

    BitmapFont getFont(int id) {
        return fonts.get(id);
    }
}
