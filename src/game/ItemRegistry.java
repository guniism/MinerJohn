package game;

import java.util.HashMap;
import java.util.Map;

public class ItemRegistry {
    private static final Map<String, Item> items = new HashMap<>();

    static {
        
        items.put("Stone", new Item( 0, 0));
        items.put("Smooth_Stone", new Item( 0, 1));
        items.put("Copper", new Item(0, 2));
        items.put("Silver", new Item( 0, 3));
        items.put("Gold", new Item(0, 4));
        items.put("Diamond", new Item(0, 5));
        items.put("Stone_Pickaxe", new Item(2, 0));
        items.put("Copper_Pickaxe", new Item(2, 1));
        items.put("Silver_Pickaxe", new Item(2, 2));
        items.put("Gold_Pickaxe", new Item(2, 3));
        items.put("Diamond_Pickaxe", new Item(2, 4));
        items.put("Stone_Shovel", new Item(2, 5));
        items.put("Copper_Shovel", new Item(3, 0));
        items.put("Silver_Shovel", new Item(3, 1));
        items.put("Gold_Shovel", new Item(3, 2));
        items.put("Diamond_Shovel", new Item(3, 3));
        
    }

    public static Item getItemById(String id) {
        return items.get(id);
    }
}
