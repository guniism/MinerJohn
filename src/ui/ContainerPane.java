package ui;

import game.GameController;
import entities.Player;
import game.Item;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.*;

public class ContainerPane extends Canvas {

    private Image spriteSheet;
    private Image itemSprite;
    public int currentState;
    private boolean isBag;

    private static final int TILE_WIDTH = 22;
    private static final int TILE_HEIGHT = 22;
    private static final int ITEM_SIZE = 16;
    private static final int SCALE = GameController.getScale();
    private static final int SPRITE_TILE_SIZE = 16;
    private int row;
    private int col;
    public Item item;

    public ContainerPane(boolean isBag, int row, int col) {
        this.isBag = isBag;
        this.currentState = 0;
        this.row = row;
        this.col = col;
        String path = ClassLoader.getSystemResource("button_ui.png").toString();
        spriteSheet = new Image(path);

        this.setWidth(TILE_WIDTH * SCALE);
        this.setHeight(TILE_HEIGHT * SCALE);

        loadItemFromInventory();
        drawContainer();

        this.setOnMouseClicked(e -> toggleSelectedState());
        this.setOnMouseEntered(e -> this.setCursor(javafx.scene.Cursor.HAND));
        this.setOnMouseExited(e -> this.setCursor(javafx.scene.Cursor.DEFAULT));

        this.setOnDragDetected(e -> startDrag(e));

        this.setOnDragOver(e -> allowDrop(e));
        this.setOnDragDropped(e -> handleDrop(e));
    }

    private void toggleSelectedState() {
        if (currentState == 0) {
            setContainerState(1);
        } else {
            setContainerState(0);
        }
        
        if (currentState == 1) {
            Player.setUsingItem(item);
        }
    }

    public void setContainerState(int state) {
        this.currentState = state;
        drawContainer();
    }

    public void loadItemFromInventory() {
        item = Player.getInventory().get(row).get(col);
        if (item != null) {
            String itemPath = ClassLoader.getSystemResource("item-sprite.png").toString();
            itemSprite = new Image(itemPath);
        } else {
            itemSprite = null;
        }
        
        if (currentState == 1) {
            Player.setUsingItem(item);
        }
    }

    public void drawContainer() {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setImageSmoothing(false);
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());

        double srcX = 0;
        double srcY = 0;

        if (isBag) {
            srcX = (currentState == 0) ? 44 : 66;
        } else {
            srcX = (currentState == 0) ? 0 : 22;
        }

        gc.drawImage(spriteSheet, srcX, srcY, TILE_WIDTH, TILE_HEIGHT, 0, 0, TILE_WIDTH * SCALE, TILE_HEIGHT * SCALE);

        if (item != null && itemSprite != null) {
            int itemRow = item.getRow();
            int itemCol = item.getCol(); 

            double srcItemX = itemCol * SPRITE_TILE_SIZE; 
            double srcItemY = itemRow * SPRITE_TILE_SIZE; 

            double itemScaledSize = ITEM_SIZE * SCALE;
            double centerX = (TILE_WIDTH * SCALE - itemScaledSize) / 2;
            double centerY = (TILE_HEIGHT * SCALE - itemScaledSize) / 2;

            gc.drawImage(itemSprite, srcItemX, srcItemY, SPRITE_TILE_SIZE, SPRITE_TILE_SIZE, centerX, centerY, itemScaledSize, itemScaledSize);
        }
    }


    private void startDrag(MouseEvent event) {
        if (item != null) {
            Dragboard db = this.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(row + "," + col);
            db.setContent(content);
            event.consume();
        }
    }

    private void allowDrop(DragEvent event) {
        event.acceptTransferModes(TransferMode.MOVE);
        event.consume();
    }

    private void handleDrop(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasString()) {
            String[] pos = db.getString().split(",");
            int sourceRow = Integer.parseInt(pos[0]);
            int sourceCol = Integer.parseInt(pos[1]);

            Item sourceItem = Player.getInventory().get(sourceRow).get(sourceCol);
            Item targetItem = Player.getInventory().get(row).get(col);

            Player.getInventory().get(sourceRow).set(sourceCol, targetItem);
            Player.getInventory().get(row).set(col, sourceItem);

            Player.containerGrid[sourceRow][sourceCol].loadItemFromInventory();
            Player.containerGrid[sourceRow][sourceCol].drawContainer();

            this.loadItemFromInventory();
            this.drawContainer();
            
            if (currentState == 1) {
                Player.setUsingItem(item);
            }
        }
        event.setDropCompleted(true);
        event.consume();
    }
}
