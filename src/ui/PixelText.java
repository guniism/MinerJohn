package ui;

import game.GameController;
import javafx.scene.layout.HBox;
import utils.SpriteSheet;

public class PixelText extends HBox {
	private int charSumWidth;
	private final int CHAR_HEIGHT = 5;
	private static int[][] spriteData = { { 1, 5, 1, 1 }, // ' '
			{ 1, 5, 4, 1 }, // '!'
			{ 3, 5, 7, 1 }, // '"'
			{ 5, 5, 12, 1 }, // '#'
			{ 3, 7, 19, 0 }, // '$'
			{ 5, 5, 24, 1 }, // '%'
			{ 4, 5, 31, 1 }, // '&'
			{ 1, 5, 37, 1 }, // '''
			{ 2, 5, 40, 1 }, // '('
			{ 2, 5, 44, 1 }, // ')'
			{ 3, 5, 48, 1 }, // '*'
			{ 3, 5, 53, 1 }, // '+'
			{ 1, 6, 58, 1 }, // ','
			{ 3, 5, 61, 1 }, // '-'
			{ 1, 5, 66, 1 }, // '.'3
			{ 5, 5, 69, 1 }, // '/'
			{ 3, 5, 1, 8 }, // '0'
			{ 3, 5, 6, 8 }, // '1'
			{ 3, 5, 11, 8 }, // '2'
			{ 3, 5, 16, 8 }, // '3'
			{ 3, 5, 21, 8 }, // '4'
			{ 3, 5, 26, 8 }, // '5'
			{ 3, 5, 31, 8 }, // '6'
			{ 3, 5, 36, 8 }, // '7'
			{ 3, 5, 41, 8 }, // '8'
			{ 3, 5, 46, 8 }, // '9'
			{ 1, 5, 51, 8 }, // ':'
			{ 1, 5, 54, 8 }, // ';'
			{ 3, 5, 57, 8 }, // '<'
			{ 3, 5, 62, 8 }, // '='
			{ 3, 5, 67, 8 }, // '>'
			{ 3, 5, 72, 8 }, // '?'
			{ 6, 7, 77, 7 }, // '@'
			{ 4, 5, 1, 15 }, // 'A'
			{ 4, 5, 7, 15 }, // 'B'
			{ 4, 5, 13, 15 }, // 'C'
			{ 4, 5, 19, 15 }, // 'D'
			{ 3, 5, 25, 15 }, // 'E'
			{ 3, 5, 30, 15 }, // 'F'
			{ 4, 5, 35, 15 }, // 'G'
			{ 4, 5, 41, 15 }, // 'H'
			{ 3, 5, 47, 15 }, // 'I'
			{ 3, 5, 52, 15 }, // 'J'
			{ 3, 5, 57, 15 }, // 'K'
			{ 3, 5, 62, 15 }, // 'L'
			{ 5, 5, 67, 15 }, // 'M'
			{ 4, 5, 74, 15 }, // 'N'
			{ 4, 5, 80, 15 }, // 'O'
			{ 3, 5, 86, 15 }, // 'P'
			{ 5, 5, 91, 15 }, // 'Q'
			{ 3, 5, 1, 22 }, // 'R'
			{ 3, 5, 6, 22 }, // 'S'
			{ 3, 5, 11, 22 }, // 'T'
			{ 4, 5, 16, 22 }, // 'U'
			{ 3, 5, 22, 22 }, // 'V'
			{ 5, 5, 27, 22 }, // 'W'
			{ 4, 5, 34, 22 }, // 'X'
			{ 3, 5, 40, 22 }, // 'Y'
			{ 3, 5, 45, 22 }, // 'Z'
			{ 2, 5, 50, 22 }, // '['
			{ 5, 5, 54, 22 }, // '\'
			{ 2, 5, 61, 22 }, // ']'
			{ 3, 5, 65, 22 }, // '^'
			{ 3, 5, 70, 22 }, // '_'
			{ 2, 5, 75, 22 }, // '`'
			{ 4, 5, 1, 15 }, // 'a'
			{ 4, 5, 7, 15 }, // 'b'
			{ 4, 5, 13, 15 }, // 'c'
			{ 4, 5, 19, 15 }, // 'd'
			{ 3, 5, 25, 15 }, // 'e'
			{ 3, 5, 30, 15 }, // 'f'
			{ 4, 5, 35, 15 }, // 'g'
			{ 4, 5, 41, 15 }, // 'h'
			{ 3, 5, 47, 15 }, // 'i'
			{ 3, 5, 52, 15 }, // 'j'
			{ 3, 5, 57, 15 }, // 'k'
			{ 3, 5, 62, 15 }, // 'l'
			{ 5, 5, 67, 15 }, // 'm'
			{ 4, 5, 74, 15 }, // 'n'
			{ 4, 5, 80, 15 }, // 'o'
			{ 3, 5, 86, 15 }, // 'p'
			{ 5, 5, 91, 15 }, // 'q'
			{ 3, 5, 1, 22 }, // 'r'
			{ 3, 5, 6, 22 }, // 's'
			{ 3, 5, 11, 22 }, // 't'
			{ 4, 5, 16, 22 }, // 'u'
			{ 3, 5, 22, 22 }, // 'v'
			{ 5, 5, 27, 22 }, // 'w'
			{ 4, 5, 34, 22 }, // 'x'
			{ 3, 5, 40, 22 }, // 'y'
			{ 3, 5, 45, 22 }, // 'z'
			{ 3, 5, 79, 22 }, // '{'
			{ 1, 6, 84, 22 }, // '|'
			{ 3, 5, 87, 22 }, // '}'
			{ 4, 5, 92, 22 } // '~'
	};

	public PixelText(String text) {	
		setText(text);
	}

	public void setText(String text) {
		this.charSumWidth = 0;
		this.getChildren().clear();
		this.setSpacing(GameController.getScale());
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			int dataIndex = (int) c - ' ';
			drawText(spriteData[dataIndex][0], spriteData[dataIndex][1], spriteData[dataIndex][2], spriteData[dataIndex][3]);
		}
        this.setWidth(( this.charSumWidth * GameController.getScale()) + ((text.length() - 1) * GameController.getScale()));
        this.setHeight(CHAR_HEIGHT * GameController.getScale());
	}

	private void drawText(int width, int height, int col, int row) { 
		SpriteSheet font = new SpriteSheet("fonts-sprite.png", width, height, row, col, 1);	
		this.getChildren().add(font);
		if (height == 7) {
		    font.setTranslateY(font.getTranslateY() - GameController.getScale());
		}
		charSumWidth += width;
	}

}
