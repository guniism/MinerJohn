package utils;

import game.GameController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SpriteSheet extends Canvas {
    private Image spriteSheet;
    private int tileWidth, tileHeight;

    public SpriteSheet(String filename, int tileWidth, int tileHeight, int r, int c, int mode) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        String path = ClassLoader.getSystemResource(filename).toString();
        this.spriteSheet = new Image(path);

        this.setWidth(tileWidth * GameController.getScale());
        this.setHeight(tileHeight * GameController.getScale());

        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setImageSmoothing(false);
        gc.scale(GameController.getScale(), GameController.getScale());

        drawSprite(r, c, mode);
    }

    public void setSprite(String filename, int tileWidth, int tileHeight, int r, int c, int mode) {
        String path = ClassLoader.getSystemResource(filename).toString();
        if (!this.spriteSheet.getUrl().equals(path)) {
            this.spriteSheet = new Image(path);
        }

        drawSprite(r, c, mode);
    }

    private void drawSprite(int r, int c, int mode) {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        double srcX = c;
        double srcY = r;

        if (mode == 0) {
            srcX *= tileWidth;
            srcY *= tileHeight;
        }

        gc.drawImage(spriteSheet, srcX, srcY, tileWidth, tileHeight, 0, 0, tileWidth, tileHeight);
    }
}
