package world;

import game.GameController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Block extends Canvas {
	private static final int SIZE = 16;
    protected Image spriteSheet;
    protected int tileCol,tileRow;

    public Block(String imagePath) {
        String path = ClassLoader.getSystemResource(imagePath).toString();
        this.spriteSheet = new Image(path);

        this.setWidth(16 * GameController.getScale());
        this.setHeight(16 * GameController.getScale());
//
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setImageSmoothing(false);
        gc.scale(GameController.getScale(), GameController.getScale());

//        render(gc);
    }

    protected abstract void render(GraphicsContext gc);
    
    
	public int size() {
		return SIZE;
	}
}