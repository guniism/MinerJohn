package world;

import javafx.scene.canvas.GraphicsContext;
import java.util.HashMap;
import java.util.Map;

public class Ore extends Block implements Pickaxeable{
	private static final Map<Integer, int[]> ORE_MAP = new HashMap<>();
	private int oreId; 
	private int health;
	private int itemCol;
	private int itemRow;
	static {
		ORE_MAP.put(0, new int[] { 0, 0, 3, 2, 2}); // Rock
		ORE_MAP.put(1, new int[] { 1, 0, 3, 2, 2}); // SmoothStone
		ORE_MAP.put(2, new int[] { 2, 0, 4, 2, 3});; // Copper
		ORE_MAP.put(3, new int[] { 3, 0, 0, 3, 5}); // Iron
		ORE_MAP.put(4, new int[] { 4, 0, 2, 3, 7});; // Gold
		ORE_MAP.put(5, new int[] { 5, 0, 4, 3, 10}); // Diamond
	}


	public Ore(int oreId) {
	    super("block-sprite.png");
	    
	    if (ORE_MAP.containsKey(oreId)) {
	    	this.oreId = oreId;
	        this.tileCol = ORE_MAP.get(oreId)[0];
	        this.tileRow = ORE_MAP.get(oreId)[1];
	        this.itemCol = ORE_MAP.get(oreId)[2];
	        this.itemRow = ORE_MAP.get(oreId)[3];
	        this.health = ORE_MAP.get(oreId)[4];
	    } else {
	    	this.tileCol = ORE_MAP.get(0)[0];
	        this.tileRow = ORE_MAP.get(0)[1];
	        this.itemCol = ORE_MAP.get(0)[2];
	        this.itemRow = ORE_MAP.get(0)[3];
	        this.health = ORE_MAP.get(0)[4];
	    }

	    GraphicsContext gc = this.getGraphicsContext2D();
	    render(gc);
	}


	@Override
	protected void render(GraphicsContext gc) {
		int tileWidth = 16;
		int tileHeight = 16;

		double srcX = this.tileCol * tileWidth; 
		double srcY =  this.tileRow * tileHeight;
		double destX = 0;
		double destY = 0;


		gc.drawImage(spriteSheet, srcX, srcY, tileWidth, tileHeight, destX, destY, tileWidth, tileHeight);
	}

	@Override
	public boolean isBrokeFromBreak(int damage) {
		// TODO Auto-generated method stub
		if(this.health - damage <= 0) {
			this.health = 0;
			return true;
		}
		this.health =  this.health - damage;
		return false;
	}
	
	public int getOreId() {
		return oreId;
	}

	public void setOreId(int oreId) {
		this.oreId = oreId;
	}

	public int getItemCol() {
		return itemCol;
	}

	public int getItemRow() {
		return itemRow;
	}

	
}
