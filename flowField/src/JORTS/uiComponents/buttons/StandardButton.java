package JORTS.uiComponents.buttons;

import org.jsfml.system.Vector2i;

public class StandardButton extends uiButton{
	public static Vector2i size = new Vector2i(100,50);
	public StandardButton(String title) {
		this(title,size.x,size.y);
	}
	public StandardButton(String title,int x,int y)
	{
		super(title,x,y);
	}
}
