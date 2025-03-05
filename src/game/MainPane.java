package game;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Pane;
import ui.*;
import utils.SpriteSheet;
import entities.Player;

public class MainPane extends Pane {
    private static int FloorNum;
    private static PixelText floorText;
    private GamePane gamePane;
    static SpriteSheet banner;
    private Bar HBar, SBar;
    private SpriteSheet inv;
	private Bag bag;
	private CloseButtonPane closeButton;
	private SpriteSheet yesButton = new SpriteSheet("menu-sprite.png", 48, 16, 2, 0, 0);
	private SpriteSheet noButton = new SpriteSheet("menu-sprite.png", 48, 16, 2, 0, 0);
	private SpriteSheet bigBar = new SpriteSheet("menu-sprite.png", 100, 32, 0, 0, 1);
	private PixelText bigText = new PixelText("RETURN TO MAIN MENU?");
	private PixelText yesText = new PixelText("YES");
	private PixelText noText = new PixelText("NO");
	
    public MainPane() {
        gamePane = new GamePane();
        this.getChildren().add(gamePane);

        List<ContainerPane> allButtons = new ArrayList<>();
        Inventory inventory = new Inventory(false, 0, allButtons);
        inventory.setLayoutX(265);
        inventory.setLayoutY(600);
        this.getChildren().add(inventory);
        
        FloorNum = 1;
        createFloorBanner();
        SpriteSheet a = new SpriteSheet("stat-ui-sprite.png", 32, 16, 0, 16, 1);       
        a.setLayoutX(15);
        a.setLayoutY(635);
        
        SpriteSheet bagIcon = new SpriteSheet("stat-ui-sprite.png", 16, 16, 0, 0, 1);      
        bagIcon.setLayoutX(995);
        bagIcon.setLayoutY(635);
        
        SpriteSheet icon = new SpriteSheet("stat-ui-sprite.png", 16, 17, 16, 0, 1);
        icon.setLayoutX(15);
        icon.setLayoutY(20);
        this.getChildren().addAll(bagIcon, icon, a);

        this.HBar= new Bar(getGamePane().getPlayer().getMaxHealth(), 0);
        this.HBar.setLayoutX(100);
        this.HBar.setLayoutY(20);
        
        this.SBar = new Bar(getGamePane().getPlayer().getMaxStamina(), 1);
        this.SBar.setLayoutX(100);
        this.SBar.setLayoutY(65);
        this.getChildren().addAll(HBar, SBar);
        
        yesButton.setOnMouseEntered(e -> yesButton.setSprite("menu-sprite.png", 48, 16, 2, 1, 0));
    	yesButton.setOnMouseExited(e -> yesButton.setSprite("menu-sprite.png", 48, 16, 2, 0, 0));
    	yesButton.setOnMousePressed(e -> yesButton.setSprite("menu-sprite.png", 48, 16, 2, 2, 0));
    	yesButton.setOnMouseReleased(e -> {
    		this.getChildren().removeAll(yesButton, noButton, bigBar, bigText, yesText, noText);
    		KeyboardController.setEsc(false);
    		GameController.goToStartPage();
    		GameController.getGamePane().getIngamesound().stop();
    	});
    	yesText.setMouseTransparent(true);
    	noText.setMouseTransparent(true);
    	noButton.setOnMouseEntered(e -> noButton.setSprite("menu-sprite.png", 48, 16, 2, 1, 0));
    	noButton.setOnMouseExited(e -> noButton.setSprite("menu-sprite.png", 48, 16, 2, 0, 0));
    	noButton.setOnMousePressed(e -> noButton.setSprite("menu-sprite.png", 48, 16, 2, 2, 0));
    	noButton.setOnMouseReleased(e -> {
    		this.getChildren().removeAll(yesButton, noButton, bigBar, bigText, yesText, noText);
    		KeyboardController.setEsc(false);
    	});
        
//        Player.addItem(new Item(0, 0), Player.containerGrid);
        Player.containerGrid[0][0].setContainerState(1); 
        Player.addItem(ItemRegistry.getItemById("Sword"), Player.containerGrid);
        Player.addItem(ItemRegistry.getItemById("Pickaxe"), Player.containerGrid);
        Player.setUsingItem(Player.containerGrid[0][0].item);
    }
    
    private void createFloorBanner() {
    	Pane floorBanner = new Pane();
        banner = new SpriteSheet("stat-ui-sprite.png", 16, 11, 33, 0, 1);

    	floorText = new PixelText("1");
    	floorText.setText("1");
    	
    	floorText.setLayoutX((banner.getWidth() / 2) - floorText.getWidth() / 2);
    	floorText.setLayoutY((banner.getHeight() / 2) - floorText.getHeight() / 2);
    	floorBanner.setLayoutX(985);
    	floorBanner.setLayoutY(20);
    	floorBanner.getChildren().add(banner);
    	floorBanner.getChildren().add(floorText);
   
    	this.getChildren().add(floorBanner);
    }
    
    public GamePane getGamePane() {
        return gamePane;
    }
    
	public static int getFloorNum() {
		return FloorNum;
	}

	public static void setFloorNum(int floorNum) {
		FloorNum = floorNum;
		floorText.setText(floorNum + "");
		floorText.setLayoutX((banner.getWidth() / 2) - floorText.getWidth() / 2);
    	floorText.setLayoutY((banner.getHeight() / 2) - floorText.getHeight() / 2);
	}

	public void setHBar(int value) {
		HBar.setBar(value);
	}

	public void setSBar(int value) {
		SBar.setBar(value);
	}

	public Object getHBar() {
		return HBar;
	}
	
	
	public void createBag(boolean create) {
		if (create) {
			if (bag == null) {
				bag = new Bag();
			}
			inv = new SpriteSheet("ingame_button.png", 55, 11, 1, 0, 0);
			inv.setLayoutX(265);
			inv.setLayoutY(60);
			bag.setLayoutX(265);
			bag.setLayoutY(115);
			closeButton = new CloseButtonPane(this, bag, inv);
			closeButton.setLayoutX(760);
			closeButton.setLayoutY(60);

			if (!this.getChildren().contains(bag)) {
				this.getChildren().add(bag);
			}

			this.getChildren().addAll(inv, closeButton);

		} else {
			if (this.getChildren().contains(bag)) {
				this.getChildren().removeAll(inv, bag, closeButton);
			}
			bag = null;
			inv = null;
			closeButton = null;
		}
	}

	public void createEsc(boolean esc) {
		if (esc) {
			bigBar.setLayoutX(290);
			bigBar.setLayoutY(210);

			bigText.setLayoutX(330);
			bigText.setLayoutY(280);

			yesButton.setLayoutX(290);
			yesButton.setLayoutY(400);

			noButton.setLayoutX(550);
			noButton.setLayoutY(400);

			yesText.setLayoutX(380);
			yesText.setLayoutY(427);

			noText.setLayoutX(645);
			noText.setLayoutY(427);

			if (!this.getChildren().contains(bigBar)) {
				this.getChildren().addAll(yesButton, noButton, bigBar, bigText, yesText, noText);
			}
		} else {
			if (this.getChildren().contains(bigBar)) {
				this.getChildren().removeAll(yesButton, noButton, bigBar, bigText, yesText, noText);
			}

		}
	}
}
