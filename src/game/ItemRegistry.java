package game;

import java.util.HashMap;
import java.util.Map;

public class ItemRegistry {
    private static final Map<String, Item> items = new HashMap<>();

    static {
        
//        items.put("Stone_Block", new Item( 0, 0));
//        items.put("Smooth_Stone", new Item( 0, 1));
//        items.put("Copper", new Item(0, 2));
//        items.put("Silver", new Item( 0, 3));
//        items.put("Gold", new Item(0, 4));
//        items.put("Diamond", new Item(0, 5));
        items.put("Pickaxe", new Item(0, 0));
        items.put("Copper_Pickaxe", new Item(0, 1));
        items.put("Silver_Pickaxe", new Item(0, 2));
        items.put("Gold_Pickaxe", new Item(0, 3));
        items.put("Diamond_Pickaxe", new Item(0, 4));
        items.put("Hoe", new Item(0, 5));
        items.put("Copper_Hoe", new Item(1, 0));
        items.put("Silver_Hoe", new Item(1, 1));
        items.put("Gold_Hoe", new Item(1, 2));
        items.put("Diamond_Hoe", new Item(1, 3));
        items.put("Sword", new Item(1, 4));
        items.put("Copper_Sword", new Item(1, 5));
        items.put("Iron_Sword", new Item(2, 0));
        items.put("Gold_Sword", new Item(2, 1));
        items.put("Diamond_Sword", new Item(2, 2));
        items.put("Stone_Ore", new Item(2, 3));
        items.put("Copper_Ore", new Item(2, 4));
        items.put("Copper_Bar", new Item(2, 5));
        items.put("Iron_Ore", new Item(3, 0));
        items.put("Iron_Bar", new Item(3, 1));
        items.put("Gold_Ore", new Item(3, 2));
        items.put("Gold_Bar", new Item(3, 3));
        items.put("Diamond_Ore", new Item(3, 4));
        items.put("Diamond_Bar", new Item(3, 5));
        
    }

    public static Item getItemById(String id) {
        return items.get(id);
    }
}
