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
    
    public MainPane() {
        gamePane = new GamePane(this);
        this.getChildren().add(gamePane);

        List<ContainerPane> allButtons = new ArrayList<>();
        Inventory inventory = new Inventory(false, 0, allButtons);
        inventory.setLayoutX(265);
        inventory.setLayoutY(600);
        this.getChildren().add(inventory);
        
        FloorNum = 1;
        createFloorBanner();
        SpriteSheet escIcon = new SpriteSheet("stat-ui-sprite.png", 32, 16, 0, 16, 1);       
        escIcon.setLayoutX(15);
        escIcon.setLayoutY(635);
        
        SpriteSheet bagIcon = new SpriteSheet("stat-ui-sprite.png", 16, 16, 0, 0, 1);      
        bagIcon.setLayoutX(995);
        bagIcon.setLayoutY(635);
        
        SpriteSheet icon = new SpriteSheet("stat-ui-sprite.png", 16, 17, 16, 0, 1);
        icon.setLayoutX(15);
        icon.setLayoutY(20);
        this.getChildren().addAll(bagIcon, icon, escIcon);

        this.HBar= new Bar(getGamePane().getPlayer().getMaxHealth(), 0);
        this.HBar.setLayoutX(100);
        this.HBar.setLayoutY(20);
        
        this.SBar = new Bar(getGamePane().getPlayer().getMaxStamina(), 1);
        this.SBar.setLayoutX(100);
        this.SBar.setLayoutY(65);
        this.getChildren().addAll(HBar, SBar);
        
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
    
    // Getter method to retrieve the stored GamePane
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
	


	

	
}
