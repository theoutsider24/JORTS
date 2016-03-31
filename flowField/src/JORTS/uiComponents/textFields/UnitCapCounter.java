package JORTS.uiComponents.textFields;

import static JORTS.common.Constants.RESOLUTION_X;
import static JORTS.common.Constants.UPPER_GUI_HEIGHT;

import org.jsfml.system.Vector2f;

import JORTS.core.GameWindow;

public class UnitCapCounter extends UpdatableTextField{
	GameWindow window;
	int width=100;
	int offset =-200;
	public UnitCapCounter(GameWindow window) 
	{
		super();
		this.window=window;

		
		
		setSize(new Vector2f(width,UPPER_GUI_HEIGHT));
		setOrigin(new Vector2f(width/2,0));
		setPosition((RESOLUTION_X/2)+offset,0);
		
		
		text.setCharacterSize(20);
	}
	@Override
	public void update() 
	{	
		if(!text.getString().equals(window.activePlayer.unitCount+"/"+window.activePlayer.unitCap))
		{setText(window.activePlayer.unitCount+"/"+window.activePlayer.unitCap);
		setCentered(true);
		text.move(new Vector2f(-width/2,0));}
	}
}
