package uiComponents.textFields;

import static common.Constants.*;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Text;
import org.jsfml.system.Clock;
import org.jsfml.system.Vector2f;

import FYP.Main;

public class GameClock extends UpdatableTextField
{
	public Clock clock;
	int width=100;
	public GameClock()
	{
		super();
		setSize(new Vector2f(width,UPPER_GUI_HEIGHT));
		setOrigin(new Vector2f(width/2,0));
		setPosition(RESOLUTION_X/2,0);
		
		text.setCharacterSize(20);
		text.setPosition((RESOLUTION_X/2)-30,0);
		
		clock=new Clock();
		clock.restart();
	}
	@Override
	public void update() {
		String minutes = ""+(int)(clock.getElapsedTime().asSeconds()/60);
		if(minutes.length()<2)minutes="0"+minutes;
		String seconds = ""+(int)(clock.getElapsedTime().asSeconds()%60);
		if(seconds.length()<2)seconds="0"+seconds;
		text.setString(minutes+":"+seconds);
	}

	@Override
	public String toString()
	{
		return "clock";
	}
}