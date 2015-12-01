package uiComponenents.textFields;

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

public class FPSTimer extends UpdatableTextField
{
	Clock clock;
	int width=100;
	int offset =200;

	int averageCounter=30;
	int count;
	long time;
	public FPSTimer()
	{
		super();
		setSize(new Vector2f(width,UPPER_GUI_HEIGHT));
		setOrigin(new Vector2f(width/2,0));
		setPosition((RESOLUTION_X/2)+offset,0);
		
		text.setCharacterSize(20);
		text.setPosition(((RESOLUTION_X/2)-35)+offset,0);
		
		clock=new Clock();
		clock.restart();
	}
	@Override
	public void update() {
		time+=clock.getElapsedTime().asMilliseconds();
		count++;
		if(count>averageCounter)
		{			
			String fps =""+ (int)(1000.0/(time/averageCounter));
			count=0;
			time=0;
			setText(fps+" FPS");
		}
		clock.restart();
	}
	@Override
	public String toString()
	{
		return "fpstimer";
	}
}
