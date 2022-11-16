package org.madbunny.vsrat2d.api;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

public class Screen {
    private static final float DEFAULT_ALPHA_VALUE = 1;

    private final ApplicationContext ctx;
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final Matrix4 projectionMatrix;
    private final Batch textBatch = new SpriteBatch();
    private final ArrayList<RenderFontTask> renderFontTasks = new ArrayList<>();


    public final int width;
    public final int height;

    Screen(ApplicationContext ctx, int width, int height) {
        this.ctx = ctx;
        this.width = width;
        this.height = height;
        var camera = new OrthographicCamera();
        var viewport = new FitViewport(width, height, camera);
        viewport.update(width, height, true);
        projectionMatrix = camera.combined;
    }

    void onAfterFrame() {
        renderFontTasks.clear();
    }

    void renderTexts() {
        textBatch.begin();
        for (var task : renderFontTasks) {
            var color = task.color;
            task.font.setColor(new com.badlogic.gdx.graphics.Color(color.red(), color.green(), color.blue(), color.alpha()));
            task.font.draw(textBatch, task.text, task.position.x(), task.position.y() + task.font.getCapHeight());
        }
        textBatch.end();
    }

    public static void clear(Color color) {
        Gdx.gl.glClearColor(color.red(), color.green(), color.blue(), DEFAULT_ALPHA_VALUE);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void drawRectangle(Point2D bottomLeftPoint, Size2D size, Paint paint) {
        drawShape(paint, () -> shapeRenderer.rect(bottomLeftPoint.x(), bottomLeftPoint.y(), size.width(), size.height()));
    }

    public void drawCircle(Point2D center, float radius, Paint paint) {
        drawShape(paint, () -> shapeRenderer.circle(center.x(), center.y(), radius));
    }

    public void drawTriangle(Point2D a, Point2D b, Point2D c, Paint paint) {
        drawShape(paint, () -> shapeRenderer.triangle(a.x(), a.y(), b.x(), b.y(), c.x(), c.y()));
    }

    public void drawStripe(Point2D begin, Point2D end, float width, Paint paint) {
        if (width < 1.0f) {
            return;
        }
        var dir = Point2D.subtract(end, begin);
        var len = dir.norm();
        if (len <= 1e-1f) {
            return;
        }
        float halfWidth = width * 0.5f;
        float nx = halfWidth * -dir.y() / len;
        float ny = halfWidth * dir.x() / len;
        drawShape(paint, () -> {
            shapeRenderer.triangle(begin.x() + nx, begin.y() + ny, begin.x() - nx, begin.y() - ny, end.x() - nx, end.y() - ny);
            shapeRenderer.triangle(begin.x() + nx, begin.y() + ny, end.x() - nx, end.y() - ny, end.x() + nx, end.y() + ny);
        });
    }

    public void drawLine(Point2D begin, Point2D end, Color color) {
        drawShape(new Paint(color, false), () -> shapeRenderer.line(begin.x(), begin.y(), end.x(), end.y()));
    }

    public void drawText(int fontId, Point2D position, Color color, String text) {
        var font = ctx.fonts().getFont(fontId);
        renderFontTasks.add(new RenderFontTask(font, position, color, text));
    }

    private void drawShape(Paint paint, Runnable drawOperation) {
        var color = paint.color();
        shapeRenderer.setProjectionMatrix(projectionMatrix);
        shapeRenderer.begin(getShapeType(paint));
        shapeRenderer.setColor(color.red(), color.green(), color.blue(), color.alpha());
        drawOperation.run();
        shapeRenderer.end();
    }

    private static ShapeRenderer.ShapeType getShapeType(Paint paint) {
        return paint.filled() ? ShapeRenderer.ShapeType.Filled : ShapeRenderer.ShapeType.Line;
    }

    private record RenderFontTask(BitmapFont font, Point2D position, Color color, String text) {}
}
