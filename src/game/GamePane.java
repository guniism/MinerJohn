package game;

import entities.Slime;
import entities.Zombie;
import entities.FloatingItem;
import entities.Monster;
import entities.Player;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import world.Block;
import world.Interactable;
import world.Ladder;
import world.LadderUp;
import world.Map;
import world.Ore;
import world.Pickaxeable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import audio.AudioController;

public class GamePane extends Pane {
	private Map gameMap;
	private Player player;
	private double playerCenterAbsX;
	private double playerCenterAbsY;
	private int[][] mapBlock, mapMonster;
	private List<Block> blocks = new ArrayList<>();
	private List<FloatingItem> floatingItems = new ArrayList<>();
	private Rectangle transitionScreen;
	private int SLIME_COUNT = 3, ZOMBIE_COUNT = 3;
	private List<Monster> monsters;
	private int ladderX;
	private int ladderY;
	private boolean end;
	private AudioController ingamesound = new AudioController("ingamebgm_sfx");

	public GamePane() {
		this.gameMap = new Map();
		this.player = new Player();
		this.blocks = new ArrayList<>();
		this.getChildren().add(this.gameMap);
		this.playerCenterAbsX = this.player.getX() + this.getLayoutX() - 16 * GameController.getScale();
		this.playerCenterAbsY = this.player.getY() + this.getLayoutY() - 8 * GameController.getScale();
		
		
		ingamesound.setVolume(0.8f);
		ingamesound.play();
		ingamesound.loop();

		transitionScreen = new Rectangle();
		transitionScreen.setFill(Color.BLACK);
		transitionScreen.setOpacity(0);
		transitionScreen.setViewOrder(-1000);
		transitionScreen.setVisible(false);
//		this.getChildren().add(transitionScreen);
//		GameController.getMainPane().getChildren().add(transitionScreen);

		generateNextMap();
		
		setupMouseHandler();
		startTimer();

	}

	private void generateNextMap() {
		
		Random rand = new Random();
		this.gameMap.setMap(rand.nextInt(3) + 1);

		int arraySizeH = (int) (this.gameMap.getHeight() / GameController.getScale()) / 16;
		int arraySizeW = (int) (this.gameMap.getWidth() / GameController.getScale()) / 16;
		this.mapBlock = new int[arraySizeH][arraySizeW];

		// Random player start position
		int playerSpawnX = rand.nextInt((mapBlock[0].length - 3 - 1) - 3 + 1) + 3;
		int playerSpawnY = 4;

		this.player.setX((playerSpawnX - 1) * 16 * GameController.getScale());
		this.player.setY(2 * 16 * GameController.getScale());
		this.player.setLayoutX(this.player.getX());
		this.player.setLayoutY(this.player.getY());
		this.mapBlock[playerSpawnY][playerSpawnX] = -2;

		LadderUp ladderUp = new LadderUp();
		ladderUp.setLayoutX(this.player.getX() + 16 * GameController.getScale());
		ladderUp.setLayoutY(this.player.getY());
		this.getChildren().add(ladderUp);
		blocks.add(ladderUp);

		for (int i = 0; i < mapBlock.length; i++) {
			for (int j = 0; j < mapBlock[0].length; j++) {
				if (i > 3 && i < mapBlock.length - 3 && j < mapBlock[0].length - 3 && j > 2) {
					if (i == playerSpawnY && j == playerSpawnX) {
						continue;
					}
					int randomValue = rand.nextInt(100);

					int p1 = 90;
					int p2 = 4;
					int p3 = 3;
					int p4 = 2;
					int p5 = 1;

					if (randomValue < p1) {
						this.mapBlock[i][j] = 0;
						continue;
					} else if (randomValue < p1 + p2) {
						this.mapBlock[i][j] = 1;
					} else if (randomValue < p1 + p2 + p3) {
						this.mapBlock[i][j] = 2;
					} else if (randomValue < p1 + p2 + p3 + p4) {
						this.mapBlock[i][j] = 3;
					} else if (randomValue < p1 + p2 + p3 + p4 + p5) {
						this.mapBlock[i][j] = 4;
					} else {
						this.mapBlock[i][j] = 5;
					}

					Ore block = new Ore(this.mapBlock[i][j]);
					if (this.mapBlock[i][j] == 1) {
						block = new Ore(rand.nextInt(2));
					}
					block.setLayoutX(16 * GameController.getScale() * j);
					block.setLayoutY(16 * GameController.getScale() * i);
					this.getChildren().add(block);
					blocks.add(block);

				} else {
					this.mapBlock[i][j] = -1;
				}
			}
		}

		for (int i = 0; i < mapBlock.length; i++) {
			String tmp = "";
			for (int j = 0; j < mapBlock[0].length; j++) {
				tmp += this.mapBlock[i][j] + " ";
				if (this.mapBlock[i][j] == 1) {
				}
			}
			System.out.println(tmp);
		}

		int randomLadder = rand.nextInt(blocks.size());
		while (!(blocks.get(randomLadder) instanceof Pickaxeable)) {
			randomLadder = rand.nextInt(blocks.size());
		}
		int TargetLadderX = (int) (blocks.get(randomLadder).getLayoutX() / GameController.getScale() / 16);
		int TargetLadderY = (int) (blocks.get(randomLadder).getLayoutY() / GameController.getScale() / 16);
		System.out.println(TargetLadderX + " " + TargetLadderY);
		this.ladderX = TargetLadderX;
		this.ladderY = TargetLadderY;

		this.getChildren().add(this.player);
		mapMonster = mapBlock;

		monsters = new ArrayList<Monster>();
		generateRandomSlimes();
		generateRandomZombies();
	}

	public void createLadder(int gridX, int gridY) {
		Ladder ladder = new Ladder();
		ladder.setLayoutX(16 * GameController.getScale() * gridX);
		ladder.setLayoutY(16 * GameController.getScale() * gridY);

		this.getChildren().add(ladder);
		blocks.add(ladder);
	}

	private void setupMouseHandler() {
		this.setOnMouseClicked(this::handleMouseClick);
	}

	private void handleMouseClick(MouseEvent event) {

		if (interactBlockClick(event)) {
			return;
		}
		Item usingItem = Player.getUsingItem();
		if (usingItem == null) {
			System.out.println("Do nothing");
			return;
		}
		if (usingItem.getItemType() == 1) {
			setPlayerHealth(player.getHealth() + usingItem.getIncreaseHealth());
			setPlayerStamina(player.getStamina() + usingItem.getIncreaseStamina());
			Player.useItem(Player.getUsingItem(), 1);

		}
		if (usingItem.getRow() == 1 && usingItem.getCol() == 4) {
			System.out.println("player is Attacking");
			player.setAttacking(true);
			player.attack();

			// Play attack sound
			AudioController swordSound = new AudioController("sword_sfx");
			swordSound.setVolume(0.8f);
			swordSound.play();
		}

		if (player.getStamina() > 0) {
			// Only execute mining if ladder wasn’t clicked
			if (usingItem.getRow() == 0 && usingItem.getCol() == 0 && !player.isMining()) {
				System.out.println("player is Mining");
				player.setMining(true);
				player.mine();

				// play mining sound
				AudioController mineSound = new AudioController("rock_sfx");
				mineSound.setVolume(0.8f);
				mineSound.play();
			}
		} else {
			System.out.println("Player has no stamina!");
		}

	}

	private boolean interactBlockClick(MouseEvent event) {
		for (Block block : this.blocks) {
			if (block instanceof Interactable interactBlock) {
				if (event.getX() > block.getLayoutX() && event.getX() < block.getLayoutX() + block.getWidth()
						&& event.getY() > block.getLayoutY() && event.getY() < block.getLayoutY() + block.getHeight()) {
					interactBlock.response();
					return true;
				}
			}
		}
		return false;
	}

	private void generateRandomSlimes() {
		Random random = new Random();
		final int MAX_ATTEMPTS = 100;
		for (int i = 0; i < SLIME_COUNT; i++) {
			int slimeX = 0;
			int slimeY = 0;
			int attempts = 0;
			while (attempts < MAX_ATTEMPTS) {
				slimeY = random.nextInt(5, mapMonster.length - 4);
				slimeX = random.nextInt(5, mapMonster[0].length - 4);
				if (canSpawnAt(slimeX, slimeY, mapMonster)) {
					break;
				}
				attempts++;
			}
			if (attempts >= MAX_ATTEMPTS) {
				System.out.println("ไม่พบตำแหน่ง spawn สำหรับ Slime หลัง " + MAX_ATTEMPTS + " ครั้ง");
				continue; // ข้ามการ spawn monster นี้
			}
			mapMonster[slimeY][slimeX] = -3;
			int drawX = slimeX * GameController.getScale() * 16;
			int drawY = slimeY * GameController.getScale() * 16;
			Slime slime = new Slime(drawX, drawY, 2, 1, 1, player);
			monsters.add(slime);
			this.getChildren().add(slime);
		}
	}

	private void generateRandomZombies() {
		Random random = new Random();
		final int MAX_ATTEMPTS = 100;
		for (int i = 0; i < ZOMBIE_COUNT; i++) {
			int zombieX = 0;
			int zombieY = 0;
			int attempts = 0;
			while (attempts < MAX_ATTEMPTS) {
				zombieY = random.nextInt(5, mapMonster.length - 4);
				zombieX = random.nextInt(5, mapMonster[0].length - 4);
				if (canSpawnAt(zombieX, zombieY, mapMonster)) {
					break;
				}
				attempts++;
			}
			if (attempts >= MAX_ATTEMPTS) {
				System.out.println("ไม่พบตำแหน่ง spawn สำหรับ Zombie หลัง " + MAX_ATTEMPTS + " ครั้ง");
				continue;
			}
			mapMonster[zombieY][zombieX] = -3;
			int drawX = zombieX * GameController.getScale() * 16;
			int drawY = zombieY * GameController.getScale() * 16;
			Zombie zombie = new Zombie(drawX, drawY, 2, 5, 1, player);
			monsters.add(zombie);
			this.getChildren().add(zombie);
		}
	}

	private boolean canSpawnAt(int x, int y, int[][] map) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				int checkY = y + i;
				int checkX = x + j;
				if (checkY < 0 || checkY >= map.length || checkX < 0 || checkX >= map[0].length
						|| map[checkY][checkX] != 0) {
					return false;
				}
			}
		}
		return true;
	}

	private void startTimer() {
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if(!player.isDead()) {
					update();
				}
			}
		};
		timer.start();
	}

	private void update() {
		double dx = 0, dy = 0;
		boolean movingDown = false;
		boolean movingUp = false;
		boolean movingRight = false;
		boolean movingLeft = false;
		player.update();
		if (player.canMove()) {
			if (GameController.getKeyboardController().isMoveUp()) {
				if (this.player.getY() >= 2 * 16 * GameController.getScale()
						&& !isColliding(0, -this.player.getSpeed())) {
					dy -= this.player.getSpeed();
					movingUp = true;
				}
			}
			if (GameController.getKeyboardController().isMoveDown()) {
				if (this.player.getY() <= this.gameMap.getHeight() - (5 * 16 + 8) * GameController.getScale()
						&& !isColliding(0, this.player.getSpeed())) {
					dy += this.player.getSpeed();
					movingDown = true;
				}
			}
			if (GameController.getKeyboardController().isMoveLeft()) {
				if (this.player.getX() >= 2 * 16 * GameController.getScale()
						&& !isColliding(-this.player.getSpeed(), 0)) {
					dx -= this.player.getSpeed();
					movingLeft = true;
				}
			}
			if (GameController.getKeyboardController().isMoveRight()) {
				if (this.player.getX() <= this.gameMap.getWidth() - 5 * 16 * GameController.getScale()
						&& !isColliding(this.player.getSpeed(), 0)) {
					dx += this.player.getSpeed();
					movingRight = true;
				}
			}
		}

		player.setMovingDown(movingDown);
		player.setMovingUp(movingUp);
		player.setMovingRight(movingRight);
		player.setMovingLeft(movingLeft);

		// Normalize diagonal movement
		if (dx != 0 || dy != 0) {
			double length = Math.sqrt(dx * dx + dy * dy);
			dx = (dx / length) * this.player.getSpeed();
			dy = (dy / length) * this.player.getSpeed();
		}

		// Update camera position
		double newLayoutX = playerCenterAbsX - this.player.getX();
		double newLayoutY = playerCenterAbsY - this.player.getY();
		newLayoutX = Math.max(-(this.gameMap.getWidth() - (14 * 16 * GameController.getScale())),
				Math.min(0, newLayoutX));
		newLayoutY = Math.max(-(this.gameMap.getHeight() - ((10 * 16 - 8) * GameController.getScale())),
				Math.min(0, newLayoutY));
		this.setLayoutX(newLayoutX);
		this.setLayoutY(newLayoutY);
		
		this.player.setX(this.player.getX() + dx);
		this.player.setY(this.player.getY() + dy);
		this.player.setLayoutX(this.player.getX());
		this.player.setLayoutY(this.player.getY());
//		adjustViewOrder();

		// Floating item
		Iterator<FloatingItem> iterator = floatingItems.iterator();
		while (iterator.hasNext()) {
			FloatingItem item = iterator.next();
			int playerFootGridX = (int) ((this.player.getX() + (8 + 24 - 8) * GameController.getScale())
					/ GameController.getScale() / 16);
			int playerFootGridY = (int) ((this.player.getY() + (32) * GameController.getScale())
					/ GameController.getScale() / 16);
			int itemCenGridX = (int) ((item.getLayoutX() + 8 * GameController.getScale()) / GameController.getScale()
					/ 16);
			int itemCenGridY = (int) ((item.getLayoutY() + 8 * GameController.getScale()) / GameController.getScale()
					/ 16);

			if (Math.abs(playerFootGridX - itemCenGridX) <= 1 && Math.abs(playerFootGridY - itemCenGridY) <= 1) {
//				System.out.println("start follow");
				double itemX = (item.getLayoutX() + 8 * GameController.getScale());
				double itemY = (item.getLayoutY() + 8 * GameController.getScale());

				double playerCenX = (this.player.getX() + (8 + 24 - 8) * GameController.getScale());
				double playerCenY = (this.player.getY() + (32) * GameController.getScale());

				double itemDx = playerCenX - itemX;
				double itemDy = playerCenY - itemY;
				double distance = Math.sqrt(itemDx * itemDx + itemDy * itemDy);

				if (distance > 1) {
					double speed = 2;
					double moveX = (itemDx / distance) * speed;
					double moveY = (itemDy / distance) * speed;

					item.setLayoutX(item.getLayoutX() + moveX);
					item.setLayoutY(item.getLayoutY() + moveY);
				}

			}
			if (playerFootGridX == itemCenGridX && playerFootGridY == itemCenGridY
					&& Player.addItem(new Item(item.getRow(), item.getCol()))) {
				this.getChildren().remove(item);
				iterator.remove();
			}
		}

		// Update monster
		for (Monster monster : getMonsters()) {
			Platform.runLater(() -> monster.update());
		}

//		if (player.getHealth() <= 0 && !player.isDying()) {
//			// Trigger the death animation. When it's complete, reset the game.
//	        GameController.getGamePane().getIngamesound().stop();
//	        //play gameover sound
//	        AudioController gameoverSound = new AudioController("gameover_sfx");
//	        gameoverSound.setVolume(0.7f);
//	        gameoverSound.play();
//			player.die(() -> {
//				resetGame();
//			});
//			return; // Skip further update processing while death animation plays
//		}
		adjustMonsterViewOrder();
	}

	private void adjustMonsterViewOrder() {
		for (Monster monster : this.monsters) {
			if (monster instanceof Zombie) {
				// For Zombies, add an extra offset (1 * scale) to the player's Y position
				if (player.getY() + 16 * GameController.getScale() >= monster.getY()) {
					player.setViewOrder(-501); // Player in front
					monster.setViewOrder(-500); // Zombie behind
				} else {
					player.setViewOrder(-500); // Player behind
					monster.setViewOrder(-501); // Zombie in front
				}
			} else if (monster instanceof Slime) {
				// For Slimes, use a direct comparison
				if (player.getY() + 16 * GameController.getScale() >= monster.getY()) {
					player.setViewOrder(-501); // Player in front
					monster.setViewOrder(-500); // Slime behind
				} else {
					player.setViewOrder(-500); // Player behind
					monster.setViewOrder(-501); // Slime in front
				}
			} else {
				// For other monsters, you can define a default behavior
				if (player.getY() >= monster.getY()) {
					player.setViewOrder(-501);
					monster.setViewOrder(-500);
				} else {
					player.setViewOrder(-500);
					monster.setViewOrder(-501);
				}
			}
		}
	}

	// Collision Detection for Random Rocks
	private boolean isColliding(double dx, double dy) {
		double nextX = this.player.getX() + dx + 16 * GameController.getScale();
		double nextY = this.player.getY() + dy + 16 * GameController.getScale();

		double hitboxWidth = 16 * GameController.getScale();
		double hitboxHeight = 16 * GameController.getScale();

		for (Block rock : blocks) {
			double rockX = rock.getLayoutX();
			double rockY = rock.getLayoutY();
			double rockSize = 16 * GameController.getScale(); // Adjust based on rock size

			// Smaller collision box for smoother movement
			double collisionPadding = 4 * GameController.getScale();
			double rockLeft = rockX + collisionPadding;
			double rockRight = rockX + rockSize - collisionPadding;
			double rockTop = rockY - collisionPadding;
			double rockBottom = rockY;

			// Check if player's next position overlaps with rock
			if (nextX + hitboxWidth > rockLeft && nextX < rockRight && nextY + hitboxHeight > rockTop
					&& nextY < rockBottom) {
				return true; // Collision detected
			}

		}
		return false; // No collision
	}


	
	public void enterNextFloor() {

		System.out.println("Entering next floor...");
		updateOverlaySize();
		transitionScreen.setVisible(true);

		FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), transitionScreen);
		fadeIn.setFromValue(0);
		fadeIn.setToValue(1);

		PauseTransition pause = new PauseTransition(Duration.seconds(0.5));

		Transition resetFloor = new Transition() {
			{
				setCycleDuration(Duration.seconds(0.1));
			}

			@Override
			protected void interpolate(double frac) {
				if (frac == 1.0) {
					Platform.runLater(() -> {
						getChildren().remove(getPlayer());
						getChildren().removeAll(floatingItems);
						getChildren().removeAll(blocks);
						getChildren().removeAll(monsters);
						floatingItems.clear();
						blocks.clear();
						monsters.clear();
						generateNextMap();
						if (!getChildren().contains(player)) {
							getChildren().add(player);
						}
					});
				}
			}
		};

		FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.5), transitionScreen);
		fadeOut.setFromValue(1);
		fadeOut.setToValue(0);
		fadeOut.setOnFinished(event -> {
			transitionScreen.setVisible(false);
			player.setCanMove(true);
		});

		SequentialTransition transition = new SequentialTransition(fadeIn, pause, resetFloor, fadeOut);
		transition.play();

	}

	public void resetGame() {
		System.out.println("Resetting game...");
		updateOverlaySize();
		transitionScreen.toFront();
		transitionScreen.setVisible(true);
		FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.5), transitionScreen);
		fadeOut.setFromValue(0);
		fadeOut.setToValue(1);

		PauseTransition pause = new PauseTransition(Duration.seconds(1));

		// Reset logic – remove all game objects and generate new ones
		Transition resetLogic = new Transition() {
			{
				setCycleDuration(Duration.seconds(0.1));
			}

			@Override
			protected void interpolate(double frac) {
				if (frac == 1.0) {
					Platform.runLater(() -> {			
						getChildren().remove(getPlayer());
						getChildren().removeAll(blocks);
						getChildren().removeAll(monsters);
						blocks.clear();
						monsters.clear();
						generateNextMap();
						setPlayerHealth(player.getMaxHealth());
						setPlayerStamina(player.getMaxStamina());					
						MainPane.setFloorNum(1);
						if (!getChildren().contains(player)) {
							getChildren().add(player);
						}
						player.setDead(false);
						player.setDying(false);
					});
				}
			}
		};


		// Fade in from black (overlay goes from opaque to transparent)
		FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), transitionScreen);
		fadeIn.setFromValue(1);
		fadeIn.setToValue(0);
		fadeIn.setOnFinished(event -> {
			transitionScreen.setVisible(false);
//			getChildren().remove(getPlayer());
//			getChildren().removeAll(blocks);
//			getChildren().removeAll(monsters);
//			blocks.clear();
//			monsters.clear();
//			generateNextMap();
//			setPlayerHealth(player.getMaxHealth());
//			setPlayerStamina(player.getMaxStamina());					
//			MainPane.setFloorNum(1);
//			if (!getChildren().contains(player)) {
//				getChildren().add(player);
//			}
//			player.setCanMove(true);
//			player.setDead(false);
//			player.setDying(false);
//			ingamesound.play();
			player.setCanMove(true);
			ingamesound.play();
		});

		// Create a sequential transition that plays fade out, pause, reset logic, then
		// fade in
		SequentialTransition resetSequence = new SequentialTransition(fadeOut, pause, resetLogic, fadeIn);
		resetSequence.play();
		
		
	}

	private void updateOverlaySize() {
		transitionScreen.setWidth(GameController.getScreenWidth());
		transitionScreen.setHeight(GameController.getScreenHeight());
		transitionScreen.toFront();
	}

	public void reducePlayerHealth(int damage) {
		if (player.isDead())
			return;
		setPlayerHealth(player.getHealth() - damage);
	}

	public void setPlayerHealth(int health) {
		player.setHealth(health);
//		GameController.getMainPane().createResult();
		GameController.getMainPane().setHBar(player.getHealth());
	}

	public void setPlayerStamina(int stamina) {
		player.setStamina(stamina);
		GameController.getMainPane().setSBar(player.getStamina());
	}

	public List<Block> getBlocks() {
		return blocks;
	}

	public List<FloatingItem> getfloatingItems() {
		return floatingItems;
	}

	public List<Monster> getMonsters() {
		return monsters;
	}

	public int[][] getMapBlock() {
		return mapBlock;
	}

	public int getLadderX() {
		return ladderX;
	}

	public int getLadderY() {
		return ladderY;
	}

	public Player getPlayer() {
		return player;
	}

	public AudioController getIngamesound() {
		return ingamesound;
	}

	public Rectangle getTransitionScreen() {
		return transitionScreen;
	}
}