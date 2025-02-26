package entities;

import game.GameController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Character extends Canvas {
    protected double x;
    protected double y;
    protected int speed;

    public Character(double x, double y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    // Abstract method for drawing character (must be implemented by subclasses)
    protected abstract void draw();

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
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