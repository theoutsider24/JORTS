package uiComponents;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;

import static common.Constants.*;
public class PauseOverlay implements Drawable{
	RectangleShape greyOverlay;
	int grayShade=255;
	public PauseOverlay()
	{
		greyOverlay = new RectangleShape();
		greyOverlay.setSize(new Vector2f(RESOLUTION_X,RESOLUTION_Y-(LOWER_GUI_HEIGHT+UPPER_GUI_HEIGHT)));
		greyOverlay.setPosition(0,UPPER_GUI_HEIGHT);	
		greyOverlay.setFillColor(new Color(grayShade,grayShade,grayShade,50));		
	}
	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) {
		arg0.draw(greyOverlay);		
	}
}
