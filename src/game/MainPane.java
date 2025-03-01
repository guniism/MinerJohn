package game;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import ui.*;
import entities.Player;

public class MainPane extends Pane {
    private static Text floorText;
    private static int FloorNum;
    private GamePane gamePane; // Store the GamePane here
    private HealthStamBar hBar,sBar;
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
        Floor floor = new Floor();
        Icon icon = new Icon();
        
        bagIcon.setLayoutX(995);
        bagIcon.setLayoutY(635);
        
        floor.setLayoutX(985);
        floor.setLayoutY(20);
        FloorNum = 1;
        floorText = new Text("1");
        floorText.setLayoutX(floor.getLayoutX() + 33);
        floorText.setLayoutY(floor.getLayoutY() + 35);
        floorText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: black;");
        
        icon.setLayoutX(15);
        icon.setLayoutY(20);
        
        this.getChildren().addAll(bagIcon, floor, icon, floorText);
        
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
    
    // Getter method to retrieve the stored GamePane
    public GamePane getGamePane() {
        return gamePane;
    }
    
    public static void setFloorText(String newText) {
        floorText.setText(newText);
    }

	public static int getFloorNum() {
		return FloorNum;
	}

	public static void setFloorNum(int floorNum) {
		FloorNum = floorNum;
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
