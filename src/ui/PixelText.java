package ui;

import game.GameController;
import javafx.scene.layout.HBox;
import utils.SpriteSheet;

public class PixelText extends HBox{
	private final int CHAR_WIDTH = 3;
	private final int CHAR_HEIGHT = 5;
    public PixelText(String text) {
    	setText(text);
    }

    public void setText(String text) {
//    	System.out.println(text.length());
    	this.getChildren().clear();
        this.setSpacing(GameController.getScale());
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            int num = c - '0';
            if(num >= 0 && num <= 9) {
            	drawText(0, num);
            }
        }  
        this.setWidth((text.length() * CHAR_WIDTH * GameController.getScale()) + ((text.length() - 1) * GameController.getScale()));
        this.setHeight(CHAR_HEIGHT * GameController.getScale());
    }
    private void drawText(int row, int col) {
    	SpriteSheet font = new SpriteSheet("font-sprite.png", CHAR_WIDTH, CHAR_HEIGHT, row, col, 0);
    	this.getChildren().add(font);
    }
    
    
}
