package ui;

import game.GameController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Icon extends Canvas {
    private static final int TILE_WIDTH = 16;
    private static final int TILE_HEIGHT = 17;
    private static final int SCALE = GameController.getScale();

    private Image spriteSheet;

    public Icon() {
        String path = ClassLoader.getSystemResource("stat-ui-sprite.png").toString();
        spriteSheet = new Image(path);

        this.setWidth(TILE_WIDTH * SCALE);
        this.setHeight(TILE_HEIGHT * SCALE);

        drawIcon();
    }

    private void drawIcon() {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setImageSmoothing(false);
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());

        double srcX = 0;
        double srcY = 16;

        gc.drawImage(spriteSheet, srcX, srcY, TILE_WIDTH, TILE_HEIGHT, 
                     0, 0, TILE_WIDTH * SCALE, TILE_HEIGHT * SCALE);
    }

}
