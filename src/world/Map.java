package world;


import game.GameController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Map extends Canvas {
	private Image backgroundImage;
	
	public Map() {
		String path = ClassLoader.getSystemResource("map1.png").toString();
        this.backgroundImage = new Image(path);

        this.setWidth(backgroundImage.getWidth() * GameController.getScale());
        this.setHeight(backgroundImage.getHeight() * GameController.getScale());

        GraphicsContext gc = this.getGraphicsContext2D();        
        gc.setImageSmoothing(false);
        gc.scale(GameController.getScale(), GameController.getScale());
        gc.drawImage(backgroundImage, 0, 0, backgroundImage.getWidth(), backgroundImage.getHeight());
 
	}
	
	public void setMap(int num) {
		if(num < 1) {
			num = 1;
		}
		else if(num > 3) {
			num = 3;
		}
		String path = ClassLoader.getSystemResource("map" + num + ".png").toString();
        this.backgroundImage = new Image(path);
		
		this.setWidth(backgroundImage.getWidth() * GameController.getScale());
        this.setHeight(backgroundImage.getHeight() * GameController.getScale());
        

        GraphicsContext gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());
        gc.drawImage(backgroundImage, 0, 0, backgroundImage.getWidth(), backgroundImage.getHeight());
	}

}