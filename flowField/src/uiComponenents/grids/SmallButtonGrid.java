package uiComponenents.grids;

import org.jsfml.system.Vector2f;

import uiComponenents.buttons.SmallButton;

public class SmallButtonGrid extends ButtonGrid{

	public SmallButtonGrid(int x, int y, Vector2f pos) {
		super(x, y, pos,SmallButton.size);
	}

}
