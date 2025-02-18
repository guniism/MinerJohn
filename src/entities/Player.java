package entities;


import game.GameController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Player extends Canvas {
	private final int WIDTH;
	private final int HEIGHT;
	private double x;
	private double y;
	private int speed;
	
	public Player() {
		WIDTH = 16 * GameController.getScale();
		HEIGHT = 16 * GameController.getScale();
		setSpeed(10);
		setX(960/2);
		setY(720/2);
		
		draw();
	}
	
	public void draw() {
		String path = ClassLoader.getSystemResource("boy1.png").toString();
        Image boy = new Image(path);
        
        this.setWidth(boy.getWidth() * GameController.getScale());
        this.setHeight(boy.getHeight() * GameController.getScale());

        GraphicsContext gc = this.getGraphicsContext2D();        
        gc.setImageSmoothing(false);
        gc.scale(GameController.getScale(), GameController.getScale());
        gc.drawImage(boy, 0, 0, boy.getWidth(), boy.getHeight());
	}

	public double getX() {
		return x;
	}

	public void setX(double d) {
		this.x = d;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	
}
