package ui;

import game.GameController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class InventoryButton extends Canvas {
    private static final int TILE_WIDTH = 55;
    private static final int TILE_HEIGHT = 11;
    private static final int SCALE = GameController.getScale();

    private Image spriteSheet;
    public InventoryButton() {
        String path = ClassLoader.getSystemResource("ingame_button.png").toString();
        spriteSheet = new Image(path);

        this.setWidth(TILE_WIDTH * SCALE);
        this.setHeight(TILE_HEIGHT * SCALE);

        drawButton();
    }

    private void drawButton() {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setImageSmoothing(false);
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());

        double srcX = 0 * TILE_WIDTH;
        double srcY = 1 * TILE_HEIGHT;

        gc.drawImage(spriteSheet, srcX, srcY, TILE_WIDTH, TILE_HEIGHT, 0, 0, TILE_WIDTH * SCALE, TILE_HEIGHT * SCALE);
    }
}
