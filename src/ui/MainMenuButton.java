package ui;

import game.GameController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class MainMenuButton extends Canvas {

    private Image spriteSheet;
    private int buttonType;
    private int currentState;

    private static final int TILE_WIDTH = 48;
    private static final int TILE_HEIGHT = 17;

    public MainMenuButton(int buttonType) {
        this.buttonType = buttonType;
        this.currentState = 0;

        String path = ClassLoader.getSystemResource("button-sprite.png").toString();
        spriteSheet = new Image(path);

        this.setWidth(TILE_WIDTH * GameController.getScale());
        this.setHeight(TILE_HEIGHT * GameController.getScale());

        drawButton();

        this.setOnMouseEntered(e -> setButtonState(1));
        this.setOnMouseExited(e -> setButtonState(0));
        this.setOnMousePressed(e -> setButtonState(2));
        this.setOnMouseReleased(e -> setButtonState(1));
    }

    public void setButtonState(int state) {
        this.currentState = state;
        drawButton();
    }

    private void drawButton() {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setImageSmoothing(false);
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());

        double buttonSrcX = currentState * TILE_WIDTH;
        double buttonSrcY = 0;

        gc.drawImage(spriteSheet, buttonSrcX, buttonSrcY, TILE_WIDTH, TILE_HEIGHT, 0, 0, TILE_WIDTH * GameController.getScale(), TILE_HEIGHT * GameController.getScale());

        double textSrcX = buttonType * TILE_WIDTH;
        double textSrcY = TILE_HEIGHT;

        gc.drawImage(spriteSheet, textSrcX, textSrcY, TILE_WIDTH, TILE_HEIGHT, 0, 0, TILE_WIDTH * GameController.getScale(), TILE_HEIGHT * GameController.getScale());
    }
}
