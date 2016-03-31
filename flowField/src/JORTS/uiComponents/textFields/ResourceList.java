package JORTS.uiComponents.textFields;

import static JORTS.common.Constants.UPPER_GUI_HEIGHT;

import java.util.ArrayList;

import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;

import JORTS.core.GameWindow;

public class ResourceList extends RectangleShape{
	public static ArrayList<String> resources = new ArrayList<String>();
	
	int width=100;
	GameWindow window;
	ArrayList<ResourceLabel> labels = new ArrayList<ResourceLabel>();
	public ResourceList(GameWindow window)
	{
		super();
		this.window=window;
		setSize(new Vector2f(width*resources.size(),UPPER_GUI_HEIGHT));
		setPosition(100,0);
		int i=0;
		for(String s:resources)
		{
			ResourceLabel l = new ResourceLabel(window, s);
			l.setPosition(Vector2f.add(getPosition(), new Vector2f(width*i,0)));
			l.setSize(new Vector2f(width,UPPER_GUI_HEIGHT));
			
			labels.add(l);			
			i++;
		}
	}
	public void update()
	{
		for(ResourceLabel l:labels)
		{
			l.update();
		}
	}
	@Override 
	public void draw(RenderTarget arg0,RenderStates arg1)
	{
		super.draw(arg0, arg1);
		for(ResourceLabel l:labels)
		{
			arg0.draw(l);
		}
	}
}
