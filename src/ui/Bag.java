package ui;

import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;


public class Bag extends VBox {
    private static final int NUM_ROWS = 4;
    private List<ContainerPane> allBagButtons = new ArrayList<>();

    public Bag() {
        for (int i = 1; i <= NUM_ROWS; i++) {
            Inventory invRow = new Inventory(true, i, allBagButtons);
            this.getChildren().add(invRow);
        }
    }

    public void handleButtonClick(ContainerPane clickedButton) {
        allBagButtons.forEach(button -> button.setContainerState(0));
        clickedButton.setContainerState(1);
    }
}
