package ui;

import javafx.scene.layout.HBox;
import java.util.ArrayList;
import java.util.List;

public class HealthStamBar extends HBox {
    private List<HealthStamina> barSegments = new ArrayList<>();
    private boolean isHealth;
    private int maxBars;

    public HealthStamBar(boolean isHealth, int maxBars) {
        this.isHealth = isHealth;
        this.maxBars = maxBars;
        setBarAmount(maxBars);
    }

    public void setBarAmount(int amount) {
        this.getChildren().clear();
        barSegments.clear(); 

        amount = Math.max(0, Math.min(amount, maxBars));

        for (int i = 0; i < amount; i++) {
            HealthStamina bar = new HealthStamina(isHealth);
            barSegments.add(bar);
            this.getChildren().add(bar);
        }
    }

    public int getBarAmount() {
        return barSegments.size();
    }
}
