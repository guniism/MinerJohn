package game;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import ui.*;
import utils.SpriteSheet;
import entities.Player;

public class MainPane extends Pane {
	private static int FloorNum;
	private static PixelText floorText;
	private static SpriteSheet banner;
	private GamePane gamePane;
	private Bar HBar, SBar;
	private SpriteSheet inv;
	private Bag bag;
	private CloseButtonPane closeButton;
	private SpriteSheet yesButton = new SpriteSheet("menu-sprite.png", 48, 16, 2, 0, 0);
	private SpriteSheet noButton = new SpriteSheet("menu-sprite.png", 48, 16, 2, 0, 0);
	private SpriteSheet bigBar = new SpriteSheet("menu-sprite.png", 100, 32, 0, 0, 1);
	private SpriteSheet infoTextFrame = new SpriteSheet("menu-sprite.png", 108, 10, 58, 0, 1);
	private PixelText bigText = new PixelText("RETURN TO MAIN MENU?");
	private PixelText yesText = new PixelText("YES");
	private PixelText noText = new PixelText("NO");
	private PixelText infoText;

	public MainPane() {
		gamePane = new GamePane();
		this.getChildren().add(gamePane);
		this.getChildren().add(gamePane.getTransitionScreen());
		this.setBackground(new Background(new BackgroundFill(Color.web("#331B17"), null, null)));
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

		this.HBar = new Bar(getGamePane().getPlayer().getMaxHealth(), 0);
		this.HBar.setLayoutX(100);
		this.HBar.setLayoutY(20);

		this.SBar = new Bar(getGamePane().getPlayer().getMaxStamina(), 1);
		this.SBar.setLayoutX(100);
		this.SBar.setLayoutY(65);
		this.getChildren().addAll(HBar, SBar);

		infoTextFrame.setLayoutX(GameController.getScreenWidth() / 2 - infoTextFrame.getWidth() / 2);
		infoTextFrame.setLayoutY(GameController.getScreenHeight() * 0.8 - infoTextFrame.getHeight() / 2);
		this.getChildren().add(infoTextFrame);
		infoTextFrame.setVisible(false);
		
		infoText = new PixelText("");
		infoText.setLayoutX(GameController.getScreenWidth() / 2 - infoText.getWidth() / 2);
		infoText.setLayoutY(GameController.getScreenHeight() * 0.8 - infoText.getHeight() / 2);
		this.getChildren().add(infoText);

		yesText.setMouseTransparent(true);
		yesButton.setOnMouseEntered(e -> yesButton.setSprite("menu-sprite.png", 48, 16, 2, 1, 0));
		yesButton.setOnMouseExited(e -> yesButton.setSprite("menu-sprite.png", 48, 16, 2, 0, 0));
		yesButton.setOnMousePressed(e -> yesButton.setSprite("menu-sprite.png", 48, 16, 2, 2, 0));
		yesButton.setOnMouseReleased(e -> {
			this.getChildren().removeAll(yesButton, noButton, bigBar, bigText, yesText, noText);
			KeyboardController.setEsc(false);
			GameController.goToStartPage();
			GameController.getGamePane().getIngamesound().stop();
		});
		noText.setMouseTransparent(true);
		noButton.setOnMouseEntered(e -> noButton.setSprite("menu-sprite.png", 48, 16, 2, 1, 0));
		noButton.setOnMouseExited(e -> noButton.setSprite("menu-sprite.png", 48, 16, 2, 0, 0));
		noButton.setOnMousePressed(e -> noButton.setSprite("menu-sprite.png", 48, 16, 2, 2, 0));
		noButton.setOnMouseReleased(e -> {
			this.getChildren().removeAll(yesButton, noButton, bigBar, bigText, yesText, noText);
			KeyboardController.setEsc(false);
		});

		Player.getContainerGrid()[0][0].setContainerState(1);
		Player.addItem(ItemRegistry.getItemById("Sword"));
		Player.addItem(ItemRegistry.getItemById("Pickaxe"));
		Player.setUsingItem(Player.getContainerGrid()[0][0].getItem());
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
	
	public void createResult() {
		Pane resultPane = new Pane();
		SpriteSheet mainMenuButton = new SpriteSheet("menu-sprite.png", 48, 16, 2, 0, 0);
		SpriteSheet playAgainButton = new SpriteSheet("menu-sprite.png", 48, 16, 2, 0, 0);
		SpriteSheet bigBar = new SpriteSheet("menu-sprite.png", 100, 32, 0, 0, 1);
		PixelText bigText1 = new PixelText("The mine has won.");
		PixelText bigText2 = new PixelText("You fall on Floor " + getFloorNum() + ".");
		PixelText mainMenuText = new PixelText("main menu");
		PixelText playAgainText = new PixelText("play again");

		mainMenuText.setMouseTransparent(true);
		mainMenuButton.setOnMouseEntered(e -> mainMenuButton.setSprite("menu-sprite.png", 48, 16, 2, 1, 0));
		mainMenuButton.setOnMouseExited(e -> mainMenuButton.setSprite("menu-sprite.png", 48, 16, 2, 0, 0));
		mainMenuButton.setOnMousePressed(e -> mainMenuButton.setSprite("menu-sprite.png", 48, 16, 2, 2, 0));
		mainMenuButton.setOnMouseReleased(e -> {
			KeyboardController.setEsc(true);
			createEsc(KeyboardController.isEsc());
			
		});
		
		playAgainText.setMouseTransparent(true);
		playAgainButton.setOnMouseEntered(e -> playAgainButton.setSprite("menu-sprite.png", 48, 16, 2, 1, 0));
		playAgainButton.setOnMouseExited(e -> playAgainButton.setSprite("menu-sprite.png", 48, 16, 2, 0, 0));
		playAgainButton.setOnMousePressed(e -> playAgainButton.setSprite("menu-sprite.png", 48, 16, 2, 2, 0));
		playAgainButton.setOnMouseReleased(e -> {
			gamePane.resetGame();
			this.getChildren().removeAll(resultPane);
		});

		bigText1.setLayoutX(75);
		bigText1.setLayoutY(40);

		bigText2.setLayoutX(50);
		bigText2.setLayoutY(90);

		mainMenuButton.setLayoutX(0);
		mainMenuButton.setLayoutY(190);

		playAgainButton.setLayoutX(260);
		playAgainButton.setLayoutY(190);

		mainMenuText.setLayoutX(15 + 2);
		mainMenuText.setLayoutY(215);

		playAgainText.setLayoutX(275);
		playAgainText.setLayoutY(215);

		resultPane.getChildren().addAll(
		    bigBar, bigText1, bigText2, 
		    mainMenuButton, playAgainButton, 
		    mainMenuText, playAgainText
		);
		this.getChildren().add(resultPane);
		resultPane.setLayoutX(290);
		resultPane.setLayoutY(210);

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

	public void setInfoText(String text) {
		infoText.setText(text);		
		if(infoText.getWidth() < 104 * GameController.getScale()) {
			infoTextFrame.setSprite("menu-sprite.png", 108, 10, 58, 0, 1);
		}
		else {
			infoTextFrame.setSprite("menu-sprite.png", 130, 10, 48, 0, 1);
		}
		infoText.setLayoutX(GameController.getScreenWidth() / 2 - infoText.getWidth() / 2);
		infoText.setLayoutY(GameController.getScreenHeight() * 0.8 - infoText.getHeight() / 2);
		infoTextFrame.setLayoutX(GameController.getScreenWidth() / 2 - infoTextFrame.getWidth() / 2);
		infoTextFrame.setLayoutY(GameController.getScreenHeight() * 0.8 - infoTextFrame.getHeight() / 2);
	}

	public PixelText getInfoText() {
		return infoText;
	}

	public SpriteSheet getInfoTextFrame() {
		return infoTextFrame;
	}
}
