package uiComponents.buttons;

import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

public class SmallButton extends uiButton{
	public static Vector2i size = new Vector2i(25,25);
	public SmallButton(String title) {
		this(title,size.x,size.y);
	}
	public SmallButton(String title,int x,int y)
	{
		super(title,x,y);
	}
}
