package uiComponents.grids;

import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import FYP.Main;
import uiComponents.GUI;
import uiComponents.buttons.uiButton;

public class VerticalbuttonGrid extends ButtonGrid{

	public VerticalbuttonGrid(int x, int y, Vector2f pos, Vector2i size,GUI gui) 
	{
		super(y,x, pos, size,gui);
	}
	public void addButton(uiButton b,int x,int y)
	{
		super.addButton(b,y,x);
	}
}
