package game;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import ui.*;
import utils.SpriteSheet;
import entities.Player;

public class MainPane extends Pane {
//    private static Text floorText;
    private static int FloorNum;
    private static PixelText floorText;
    private GamePane gamePane; // Store the GamePane here
    private HealthStamBar hBar,sBar;
    static SpriteSheet banner;
    
    public MainPane() {
        // Create and store the GamePane
        gamePane = new GamePane(this);
        this.getChildren().add(gamePane);

        List<ContainerPane> allButtons = new ArrayList<>();
        Inventory inventory = new Inventory(false, 0, allButtons);
        inventory.setLayoutX(265);
        inventory.setLayoutY(600);
        this.getChildren().add(inventory);

        hBar = new HealthStamBar(true, 30);
        hBar.setLayoutX(100);
        hBar.setLayoutY(30);
        
        sBar = new HealthStamBar(false, 30);
        sBar.setLayoutX(100);
        sBar.setLayoutY(75);
        
        this.getChildren().addAll(hBar, sBar);
        
        BagIcon bagIcon = new BagIcon();

        Icon icon = new Icon();
        
        bagIcon.setLayoutX(995);
        bagIcon.setLayoutY(635);
        

        FloorNum = 1;
        createFloorBanner();
        
        icon.setLayoutX(15);
        icon.setLayoutY(20);
        
        this.getChildren().addAll(bagIcon, icon);
        
        
        Bar barH = new Bar();
        barH.setLayoutX(100);
        barH.setLayoutY(20);
        
        Bar barS = new Bar();
        barS.setLayoutX(100);
        barS.setLayoutY(65);
        this.getChildren().addAll(barH, barS);
        
//        Player.addItem(new Item(0, 0), Player.containerGrid);
        Player.addItem(ItemRegistry.getItemById("Pickaxe"), Player.containerGrid);
        Player.addItem(ItemRegistry.getItemById("Sword"), Player.containerGrid);
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

	public HealthStamBar gethBar() {
		return hBar;
	}

	public void sethBar(HealthStamBar hBar) {
		this.hBar = hBar;
	}

	public HealthStamBar getsBar() {
		return sBar;
	}

	public void setsBar(HealthStamBar sBar) {
		this.sBar = sBar;
	}
	
}
