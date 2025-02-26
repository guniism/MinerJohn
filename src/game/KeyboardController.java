package game;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class KeyboardController {
    private boolean moveUp = false;
    private boolean moveDown = false;
    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean attacking = false;
    private static boolean bag = false;
    public KeyboardController(){
        keyboardSetup();
        mouseSetup();
    }

    public void keyboardSetup() {
        GameController.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                if (e.getCode() == KeyCode.W) {
                    moveUp = true;
                }
                if (e.getCode() == KeyCode.S) {
                    moveDown = true;
                }
                if (e.getCode() == KeyCode.A) {
                    moveLeft = true;
                }
                if (e.getCode() == KeyCode.D) {
                    moveRight = true;
                }
                if (e.getCode() == KeyCode.E) {
                	bag = !bag;      	
                }             
            }
        });
        
        GameController.getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                if (e.getCode() == KeyCode.W) {
                    moveUp = false;
                }
                if (e.getCode() == KeyCode.S) {
                    moveDown = false;
                }
                if (e.getCode() == KeyCode.A) {
                    moveLeft = false;
                }
                if (e.getCode() == KeyCode.D) {
                    moveRight = false;
                }
            }
        });
    }
    
    public void mouseSetup() {
        GameController.getScene().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.isPrimaryButtonDown()) {
                    attacking = true;
                }
            }
        });
        
        GameController.getScene().setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (!e.isPrimaryButtonDown()) {
                    attacking = false;
                }
            }
        });
    }

    public boolean isMoveUp() {
        return moveUp;
    }

    public boolean isMoveDown() {
        return moveDown;
    }

    public boolean isMoveLeft() {
        return moveLeft;
    }

    public boolean isMoveRight() {
        return moveRight;
    }
    
    public boolean isAttacking() {
        return attacking;
    }
    
    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }
    public static void setBag(boolean bag) {
		KeyboardController.bag = bag;
	}

	public boolean isBag() {
		return bag;
	}
}