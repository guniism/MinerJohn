package game;

public class Item {
	private int row;
	private int col;
	private int itemType;
	private int increaseHealth;
	private int increaseStamina;
	private String info;
	private static int[][] itemID = { 
		{ 0, 1, 2, 3, 4, 5 }, 
		{ 6, 7, 8, 9, 10, 11 }, 
		{ 12, 13, 14, 15, 16, 17 },
		{ 18, 19, 20, 21, 22, 23 } 
	};
	private static int[][] itemData = { { 0, 0, 0 }, // 0: Pickaxe
		{ 0, 0, 0 }, // 1: Copper_Pickaxe
		{ 0, 0, 0 }, // 2: Silver_Pickaxe
		{ 0, 0, 0 }, // 3: Gold_Pickaxe
		{ 0, 0, 0 }, // 4: Diamond_Pickaxe
		{ 0, 0, 0 }, // 5: Hoe
		{ 0, 0, 0 }, // 6: Copper_Hoe
		{ 0, 0, 0 }, // 7: Silver_Hoe
		{ 0, 0, 0 }, // 8: Gold_Hoe
		{ 0, 0, 0 }, // 9: Diamond_Hoe
		{ 0, 0, 0 }, // 10: Sword
		{ 0, 0, 0 }, // 11: Copper_Sword
		{ 0, 0, 0 }, // 12: Iron_Sword
		{ 0, 0, 0 }, // 13: Gold_Sword
		{ 0, 0, 0 }, // 14: Diamond_Sword
		{ 1, 0, 1 }, // 15: Stone_Ore
		{ 1, 2, 4 }, // 16: Copper_Ore
		{ 1, 0, 0 }, // 17: Copper_Bar
		{ 1, 4, 6 }, // 18: Iron_Ore
		{ 1, 0, 0 }, // 19: Iron_Bar
		{ 1, 6, 8 }, // 20: Gold_Ore
		{ 1, 0, 0 }, // 21: Gold_Bar
		{ 1, 8, 15 }, // 22: Diamond_Ore
		{ 1, 0, 0 }, // 23: Diamond_Bar
	};
	private static String[] itemInfo = {
	        "Pickaxe: For breaking ore",
	        "Copper Pickaxe: A slightly better pickaxe",
	        "Silver Pickaxe: An improved pickaxe",
	        "Gold Pickaxe: A high-quality pickaxe",
	        "Diamond Pickaxe: The best pickaxe",
	        "Hoe: For tilling soil",
	        "Copper Hoe: A basic hoe",
	        "Silver Hoe: A more efficient hoe",
	        "Gold Hoe: A durable hoe",
	        "Diamond Hoe: The ultimate hoe",
	        "Sword: For combat",
	        "Copper Sword: A simple sword",
	        "Iron Sword: A stronger sword",
	        "Gold Sword: A powerful sword",
	        "Diamond Sword: The most powerful sword",
	        "Stone Ore: +1 Stamina",
	        "Copper Ore: +2 Health 41 Stamina",
	        "Copper Bar: Refined copper",
	        "Iron Ore: +4 Health 6 Stamina",
	        "Iron Bar: Refined iron",
	        "Gold Ore: +6 Health 8 Stamina",
	        "Gold Bar: Refined gold",
	        "Diamond Ore: +8 Health 15 Stamina",
	        "Diamond Bar: Refined diamond"
	};
	public Item(int row, int col) {
		this.row = row;
		this.col = col;
		this.itemType = itemData[itemID[row][col]][0];
		this.increaseHealth = itemData[itemID[row][col]][1];
		this.increaseStamina = itemData[itemID[row][col]][2];
		this.increaseStamina = itemData[itemID[row][col]][2];
		this.info = itemInfo[itemID[row][col]];
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public int getItemType() {
		return itemType;
	}

	public int getIncreaseHealth() {
		return increaseHealth;
	}
	public int getIncreaseStamina() {
		return increaseStamina;
	}
	
	public String getItemInfo() {
		return info;
	}

	public boolean equals(Item item) {
		return (item.getRow() == this.getRow() && item.getCol() == this.getCol());
	}
}
