package uiComponenents.grids;

import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import FYP.Main;
import uiComponenents.buttons.uiButton;

public class VerticalbuttonGrid extends ButtonGrid{

	public VerticalbuttonGrid(int x, int y, Vector2f pos, Vector2i size) 
	{
		super(y,x, pos, size);
	}
	public void addButton(uiButton b,int x,int y)
	{
		super.addButton(b,y,x);
	}
}
