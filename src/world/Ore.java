package world;

import javafx.scene.canvas.GraphicsContext;
import java.util.HashMap;
import java.util.Map;

public class Ore extends Block {
	private static final Map<Integer, int[]> ORE_MAP = new HashMap<>();
	private int oreId; 
	static {
		ORE_MAP.put(0, new int[] { 0, 0 }); // Rock
		ORE_MAP.put(1, new int[] { 1, 0 }); // SmoothStone
		ORE_MAP.put(2, new int[] { 2, 0 }); // Copper
		ORE_MAP.put(3, new int[] { 3, 0 }); // Iron
		ORE_MAP.put(4, new int[] { 4, 0 }); // Gold
		ORE_MAP.put(5, new int[] { 5, 0 }); // Diamond
	}

	public Ore(int oreId, String imagePath) {
	    super(imagePath);

	    if (ORE_MAP.containsKey(oreId)) {
	    	this.oreId = oreId;
	        this.tileCol = ORE_MAP.get(oreId)[0];
	        this.tileRow = ORE_MAP.get(oreId)[1];
	    } else {
	        this.tileCol = 1; 
	        this.tileRow = 0;
	    }

	    GraphicsContext gc = this.getGraphicsContext2D();
	    render(gc);
	}


	@Override
	protected void render(GraphicsContext gc) {
		int tileWidth = 16;
		int tileHeight = 16;

		double srcX = tileCol * tileWidth; 
		double srcY = tileRow * tileHeight;
		double destX = 0;
		double destY = 0;


		gc.drawImage(spriteSheet, srcX, srcY, tileWidth, tileHeight, destX, destY, tileWidth, tileHeight);
	}


	public int getOreId() {
		return oreId;
	}

	public void setOreId(int oreId) {
		this.oreId = oreId;
	}
}
