package uiComponents.textFields;

import static common.Constants.RESOLUTION_X;
import static common.Constants.UPPER_GUI_HEIGHT;

import org.jsfml.system.Vector2f;

import FYP.GameWindow;

public class ResourceLabel extends UpdatableTextField{

	GameWindow window;
	String resource="";
	public ResourceLabel(GameWindow window,String resource) 
	{
		super();
		this.window=window;
		this.resource=resource;
		
		text.setCharacterSize(15);
	}
	@Override
	public void update() 
	{	
		String s = resource+": "+window.activePlayer.resources.get(resource);
		if(!text.getString().equals(s))
		{
			setText(s);
			setCentered(true);
		}
	}
}
