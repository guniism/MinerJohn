package ui;

import game.GameController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Floor extends Canvas {
    private static final int TILE_WIDTH = 16;
    private static final int TILE_HEIGHT = 11;
    private static final int SCALE = GameController.getScale();

    private Image spriteSheet;

    public Floor() {
        String path = ClassLoader.getSystemResource("stat-ui-sprite.png").toString();
        spriteSheet = new Image(path);

        this.setWidth(TILE_WIDTH * SCALE);
        this.setHeight(TILE_HEIGHT * SCALE);

        drawFloor();
    }

    private void drawFloor() {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setImageSmoothing(false);
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());

        double srcX = 0;
        double srcY = 33;

        gc.drawImage(spriteSheet, srcX, srcY, TILE_WIDTH, TILE_HEIGHT, 
                     0, 0, TILE_WIDTH * SCALE, TILE_HEIGHT * SCALE);
    }

}
