package ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import utils.SpriteSheet;
import game.GameController;
import game.KeyboardController;
import javafx.scene.Cursor;

public class CloseButtonPane extends Canvas {
    private Image spriteSheet;
    private int state;
    private Pane deletePane;
    private Pane motherPane;
    private SpriteSheet inv;
    private static final int TILE_WIDTH = 11;
    private static final int TILE_HEIGHT = 11;
    private static final int SCALE = GameController.getScale();

    public CloseButtonPane(Pane motherPane, Pane deletePane,SpriteSheet inv) {
        this.motherPane = motherPane;
        this.deletePane = deletePane;
        this.state = 0; 
        this.inv=inv;
        String path = ClassLoader.getSystemResource("ingame_button.png").toString();
        spriteSheet = new Image(path);


        this.setWidth(TILE_WIDTH * SCALE);
        this.setHeight(TILE_HEIGHT * SCALE);

        
        drawButton();

        this.setOnMouseEntered(e -> {
            setState(1);
            this.setCursor(Cursor.HAND);
        });

        this.setOnMouseExited(e -> {
            setState(0);
            this.setCursor(Cursor.DEFAULT);
        });

        this.setOnMousePressed(e -> setState(2));
        this.setOnMouseReleased(e -> {
            setState(1);
            closePane();
        });
    }

    public void setState(int newState) {
        this.state = newState;
        drawButton();
    }

    private void drawButton() {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setImageSmoothing(false);
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());

        double srcX = state * TILE_WIDTH;
        double srcY = 0;

        gc.drawImage(spriteSheet, srcX, srcY, TILE_WIDTH, TILE_HEIGHT, 0, 0, TILE_WIDTH * SCALE, TILE_HEIGHT * SCALE);
    }

    private void closePane() {
        motherPane.getChildren().remove(deletePane);
        motherPane.getChildren().remove(this);
        motherPane.getChildren().remove(inv);
        KeyboardController.setBag(false);
    }
}
