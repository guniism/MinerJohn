package ui;

import audio.AudioController;
import game.GameController;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utils.SpriteSheet;

public class TutorialPage extends Pane {
    private SpriteSheet[] pages;
    private int currentPage = 0;
    private SpriteSheet pageDisplay;
    
    private AudioController menusound = new AudioController("menubgm_sfx");
    
    public TutorialPage(Stage primaryStage) {

        Canvas menuBg = new Canvas();
		String path = ClassLoader.getSystemResource("tutorial-bg.png").toString();
		Image bg = new Image(path);
		
		menusound.play();
		menusound.loop();
        menusound.setVolume(0.8f);

		menuBg.setWidth(bg.getWidth() * GameController.getScale());
		menuBg.setHeight(bg.getHeight() * GameController.getScale());

		GraphicsContext gc = menuBg.getGraphicsContext2D();
		gc.setImageSmoothing(false);
		gc.scale(GameController.getScale(), GameController.getScale());
		gc.drawImage(bg, 0, 0, bg.getWidth(), bg.getHeight());

		menuBg.setLayoutX(-20);
		this.getChildren().add(menuBg);

        SpriteSheet buttonLeft = new SpriteSheet("button_ui.png", 22, 22, 0, 7, 0);
        SpriteSheet buttonRight = new SpriteSheet("button_ui.png", 22, 22, 0, 4, 0);
        SpriteSheet exitButton = new SpriteSheet("button-sprite.png", 48, 17, 0, 0, 0);
        buttonLeft.setLayoutX(50);
        buttonLeft.setLayoutY(270);
        buttonRight.setLayoutX(920);
        buttonRight.setLayoutY(270);
        exitButton.setLayoutX(420);
        exitButton.setLayoutY(570);
        
        PixelText text = new PixelText("GO BACK");
        text.setLayoutX(465);
        text.setLayoutY(597);
        text.setMouseTransparent(true);
        pages = new SpriteSheet[] {
            new SpriteSheet("tutorial.png", 144, 87, 0, 0, 0),
            new SpriteSheet("tutorial.png", 144, 87, 1, 0, 0),
            new SpriteSheet("tutorial.png", 144, 87, 2, 0, 0),
            new SpriteSheet("tutorial.png", 144, 87, 3, 0, 0),
            new SpriteSheet("tutorial.png", 144, 87, 4, 0, 0),
            new SpriteSheet("tutorial.png", 144, 87, 5, 0, 0),
            new SpriteSheet("tutorial.png", 144, 87, 6, 0, 0),
            new SpriteSheet("tutorial.png", 144, 87, 7, 0, 0),
            new SpriteSheet("tutorial.png", 144, 87, 8, 0, 0),
            new SpriteSheet("tutorial.png", 144, 87, 9, 0, 0) 
        };

        pageDisplay = pages[currentPage];
        pageDisplay.setLayoutX(180);
        pageDisplay.setLayoutY(100);

        
        buttonLeft.setOnMouseEntered(e -> buttonLeft.setSprite("button_ui.png", 22, 22, 0, 8, 0));
        buttonLeft.setOnMouseExited(e -> buttonLeft.setSprite("button_ui.png", 22, 22, 0, 7, 0));
        buttonLeft.setOnMousePressed(e -> buttonLeft.setSprite("button_ui.png", 22, 22, 0, 9, 0));
        buttonLeft.setOnMouseReleased(e -> buttonLeft.setSprite("button_ui.png", 22, 22, 0, 7, 0));
        buttonLeft.setOnMouseClicked(e -> {
            currentPage = (currentPage - 1 + pages.length) % pages.length;
            updatePage();
        });
        

        buttonRight.setOnMouseEntered(e -> buttonRight.setSprite("button_ui.png", 22, 22, 0, 5, 0));
        buttonRight.setOnMouseExited(e -> buttonRight.setSprite("button_ui.png", 22, 22, 0, 4, 0));
        buttonRight.setOnMousePressed(e -> buttonRight.setSprite("button_ui.png", 22, 22, 0, 6, 0));
        buttonRight.setOnMouseReleased(e -> buttonRight.setSprite("button_ui.png", 22, 22, 0, 4, 0));
        buttonRight.setOnMouseClicked(e -> {
            currentPage = (currentPage + 1) % pages.length;
            updatePage();
        });
        
        exitButton.setOnMouseEntered(e -> exitButton.setSprite("button-sprite.png", 48, 17, 0, 1, 0));
        exitButton.setOnMouseExited(e -> exitButton.setSprite("button-sprite.png", 48, 17, 0, 0, 0));
        exitButton.setOnMousePressed(e -> exitButton.setSprite("button-sprite.png", 48, 17, 0, 2, 0));
        exitButton.setOnMouseReleased(e -> exitButton.setSprite("button-sprite.png", 48, 17, 0, 0, 0));
        exitButton.setOnMouseClicked(e -> {
        	menusound.stop();
            StartPage.showStartPage(primaryStage);
        });

        this.getChildren().addAll( buttonLeft, buttonRight,exitButton,text, pageDisplay);
    }


    private void updatePage() {
        this.getChildren().remove(pageDisplay);
        pageDisplay = pages[currentPage];
        pageDisplay.setLayoutX(180);
        pageDisplay.setLayoutY(100);
        this.getChildren().add(pageDisplay); 
    }

    public static void showTutorialPage(Stage primaryStage) {
        TutorialPage startPage = new TutorialPage(primaryStage);
        Scene startScene = new Scene(startPage, GameController.getScreenWidth(), GameController.getScreenHeight());
        primaryStage.setScene(startScene);
        primaryStage.show();
    }
}
