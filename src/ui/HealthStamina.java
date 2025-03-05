package ui;

import game.GameController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class HealthStamina extends Canvas {
    private static final int TILE_WIDTH = 1;
    private static final int TILE_HEIGHT = 4;
    private static final int SCALE = GameController.getScale();
    private boolean isHealth;
    private Image spriteSheet;

    public HealthStamina(boolean isHealth) {
        this.isHealth = isHealth;
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

        double srcX;
        double srcY;
        if(isHealth) {
        	srcX = 0;
            srcY = 8;
        }else {
        	srcX = 1;
            srcY = 8;
        }

        gc.drawImage(spriteSheet, srcX, srcY, TILE_WIDTH, TILE_HEIGHT, 0, 0, TILE_WIDTH * SCALE, TILE_HEIGHT * SCALE);
    }
}
