package game;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyboardController {
    private boolean moveUp = false;
    private boolean moveDown = false;
    private boolean moveLeft = false;
    private boolean moveRight = false;

    public KeyboardController(){
        keyboardSetup();
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

}
