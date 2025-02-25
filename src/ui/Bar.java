package ui;

import game.GameController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Bar extends Canvas {
    private static final int TILE_WIDTH = 34;
    private static final int TILE_HEIGHT = 8;
    private static final int SCALE = GameController.getScale();

    private Image spriteSheet;

    public Bar() {
        String path = ClassLoader.getSystemResource("bar-sprite.png").toString();
        spriteSheet = new Image(path);

        this.setWidth(TILE_WIDTH * SCALE);
        this.setHeight(TILE_HEIGHT * SCALE);

        drawBar();
    }

    private void drawBar() {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setImageSmoothing(false);
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());

        double srcX = 0;
        double srcY = 0;

        gc.drawImage(spriteSheet, srcX, srcY, TILE_WIDTH, TILE_HEIGHT, 0, 0, TILE_WIDTH * SCALE, TILE_HEIGHT * SCALE);
    }
}
