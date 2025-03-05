package ui;

import game.GameController;
import game.KeyboardController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.application.Platform;

public class BagIcon extends Canvas {
    private static final int TILE_WIDTH = 16;
    private static final int TILE_HEIGHT = 16;
    private static final int SCALE = GameController.getScale();

    private Image spriteSheet;

    public BagIcon() {
        String path = ClassLoader.getSystemResource("stat-ui-sprite.png").toString();
        spriteSheet = new Image(path);

        this.setWidth(TILE_WIDTH * SCALE);
        this.setHeight(TILE_HEIGHT * SCALE);

        drawBag();

        this.setOnMouseClicked(this::toggleBag);
    }

    private void drawBag() {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setImageSmoothing(false);
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());

        double srcX = 0; 
        double srcY = 0;

        gc.drawImage(spriteSheet, srcX, srcY, TILE_WIDTH, TILE_HEIGHT, 
                     0, 0, TILE_WIDTH * SCALE, TILE_HEIGHT * SCALE);
    }

    private void toggleBag(MouseEvent event) {
        Platform.runLater(() -> {
            boolean isBagOpen = GameController.getKeyboardController().isBag();
            GameController.getKeyboardController();
			KeyboardController.setBag(!isBagOpen); 
        });
    }
}
