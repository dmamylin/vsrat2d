import org.madbunny.vsrat2d.api.*;

public class TestScreen {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final String WINDOW_TITLE = "Tic-Tac-Toe";
    private static int fontId;

    public static void main(String[] args) {
        new Application()
                .setWindowWidth(WINDOW_WIDTH)
                .setWindowHeight(WINDOW_HEIGHT)
                .setWindowTitle(WINDOW_TITLE)
                .setInitializer(TestScreen::initialize)
                .run(TestScreen::updateFrame);
        System.exit(0);
    }

    private static void initialize(ApplicationContext ctx) {
        fontId = ctx.fonts().createFont("arial.ttf", 24);
    }

    private static void updateFrame(FrameContext ctx) {
        ctx.screen().clear(new Color(0.2f, 0.2f, 0.2f));
        ctx.screen().drawRectangle(
                new Point2D(0, 0),
                new Size2D(50, 50),
                new Paint(new Color(140, 0, 0), true));
        ctx.screen().drawRectangle(
                new Point2D(50, 50),
                new Size2D(50, 50),
                new Paint(new Color(140, 0, 0), false));
        ctx.screen().drawCircle(
                new Point2D(400, 300),
                50,
                new Paint(new Color(140, 140, 0), true));
        ctx.screen().drawLine(
                new Point2D(0, ctx.screen().height - 1),
                new Point2D(ctx.screen().width - 1, 0),
                new Color(255, 0, 0));
        ctx.screen().drawTriangle(
                new Point2D(150, 450),
                new Point2D(100, 400),
                new Point2D(100, 450),
                new Paint(new Color(0, 140, 0), true));
        ctx.screen().drawStripe(
                new Point2D(500, 500),
                new Point2D(700, 100),
                50,
                new Paint(new Color(0, 150, 150), true));

        ctx.screen().drawText(
                fontId,
                new Point2D(ctx.screen().width * 0.5f - 100, ctx.screen().height * 0.5f),
                new Color(0.0f, 0.0f, 0.0f),
                "HELLO WORLD");

        if (ctx.mouse().isButtonClicked(MouseButton.LEFT)) {
            var mousePos = ctx.mouse().getMousePosition();
            System.out.printf("%f, %f%n", mousePos.x(), mousePos.y());
        }
    }
}
