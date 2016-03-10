package uiComponents;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;

import uiComponents.textFields.UpdatableTextField;

import static common.Constants.*;
public class PauseOverlay implements Drawable{
	RectangleShape greyOverlay;
	int grayShade=255;
	UpdatableTextField pauseText;
	public PauseOverlay()
	{
		greyOverlay = new RectangleShape();
		greyOverlay.setSize(new Vector2f(RESOLUTION_X,RESOLUTION_Y-(LOWER_GUI_HEIGHT+UPPER_GUI_HEIGHT)));
		greyOverlay.setPosition(0,UPPER_GUI_HEIGHT);	
		greyOverlay.setFillColor(new Color(grayShade,grayShade,grayShade,50));		
		
		pauseText = new UpdatableTextField() {	
			@Override
			public void update() {	}
		};
		pauseText.setText("PAUSED");
		pauseText.text.setCharacterSize(30);
		pauseText.setFillColor(new Color(255,255,255,100));
		//pauseText.get
		pauseText.setPosition(RESOLUTION_X/2-pauseText.getSize().x/2,RESOLUTION_Y/2-pauseText.getSize().y/2);
		pauseText.setCentered(true);
		pauseText.setOutlineColor(new Color(100,100,100));
	}
	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) {
		arg0.draw(greyOverlay);	
		arg0.draw(pauseText);	
	}
}
