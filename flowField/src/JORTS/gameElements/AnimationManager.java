package JORTS.gameElements;

import java.util.ArrayList;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

public class AnimationManager implements Drawable{
	public static AnimationManager man=new AnimationManager();
	public static ArrayList<Animation> animations = new ArrayList<Animation>();

	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) {
		for(Animation a:animations)
			arg0.draw(a);
	}
	
}
