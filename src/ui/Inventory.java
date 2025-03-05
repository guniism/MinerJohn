package ui;

import javafx.scene.layout.GridPane;
import java.util.List;

import entities.Player;

public class Inventory extends GridPane {
	private static final int NUM_SLOTS = 5;
	private List<ContainerPane> allInvButtons;
	private boolean isBag;

	public Inventory(boolean isBag, int row, List<ContainerPane> allButtons) {
		this.allInvButtons = allButtons;
		this.isBag = isBag;
		for (int i = 0; i < NUM_SLOTS; i++) {
			ContainerPane button = new ContainerPane(isBag, row, i);

			Player.getContainerGrid()[row][i] = button;

			allInvButtons.add(button);

			button.setOnMouseClicked(e -> handleButtonClick(button));

			this.add(button, i, 0);
		}
	}

	private void handleButtonClick(ContainerPane clickedButton) {
		if (!isBag) {
			Player.setUsingItem(clickedButton.getItem());
		}
		allInvButtons.forEach(button -> button.setContainerState(0));
		clickedButton.setContainerState(1);
	}
}
