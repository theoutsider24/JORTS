package uiComponenents.grids;

import org.jsfml.system.Vector2f;

import uiComponenents.buttons.StandardButton;

public class StandardButtonGrid extends ButtonGrid{

	public StandardButtonGrid(int x, int y, Vector2f pos) {
		super(x, y, pos,StandardButton.size);
	}

}
