package JORTS.uiComponents.grids;

import org.jsfml.system.Vector2f;

import JORTS.uiComponents.GUI;
import JORTS.uiComponents.buttons.SmallButton;

public class SmallButtonGrid extends ButtonGrid{

	public SmallButtonGrid(int x, int y, Vector2f pos,GUI gui) {
		super(x, y, pos,SmallButton.size,gui);
	}

}
