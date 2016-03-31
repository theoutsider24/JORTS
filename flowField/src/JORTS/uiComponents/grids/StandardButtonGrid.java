package JORTS.uiComponents.grids;

import org.jsfml.system.Vector2f;

import JORTS.uiComponents.GUI;
import JORTS.uiComponents.buttons.StandardButton;

public class StandardButtonGrid extends ButtonGrid{

	public StandardButtonGrid(int x, int y, Vector2f pos,GUI gui) {
		super(x, y, pos,StandardButton.size,gui);
	}

}
