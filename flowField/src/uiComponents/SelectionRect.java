package uiComponents;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import FYP.Main;
import buildings.Building;
import common.CommonFunctions;
import units.Entity;

public class SelectionRect extends RectangleShape{
	boolean selectionInProgress=false;
	
	public RectangleShape realRect = new RectangleShape();
	public FloatRect globalBounds= realRect.getGlobalBounds();
	
	public SelectionRect()
	{
		setFillColor(Color.TRANSPARENT);
		setOutlineColor(Color.WHITE);
		setOutlineThickness(1);
	}
	public void start(Vector2f v)
	{
		setPosition(v);
		Main.window.setView(Main.gameView);	
		realRect.setPosition(Main.window.mapPixelToCoords(new Vector2i((int)v.x,(int)v.y)));
		selectionInProgress=true;
	}
	public void end()
	{
		setSize(new Vector2f(0,0));
		selectionInProgress=false;
	}
	public void update(Vector2f v)
	{
		if(selectionInProgress)
		{
			setSize(Vector2f.sub(v, getPosition()));
			Main.window.setView(Main.gameView);	
			Vector2f vReal= Main.window.mapPixelToCoords(new Vector2i((int)v.x,(int)v.y));
			realRect.setPosition(realRect.getPosition());
			realRect.setSize(Vector2f.sub(vReal, realRect.getPosition()));
			globalBounds= realRect.getGlobalBounds();

			//RectangleShape r=new RectangleShape();
			//FloatRect rect=realRect.getGlobalBounds();
			boolean hoverUnits=false;
			for(Entity e:Main.activePlayer.getUnits())
			{
				if(globalBounds.contains(e.getPosition().x, e.getPosition().y)||CommonFunctions.getDist(globalBounds, e.getPosition())<e.getRadius())
				{
					e.hover();
					hoverUnits=true;
				}
			}
			if(!hoverUnits)
			{
				for(Building b:Main.activePlayer.getBuildings())
				{
					if(b.getGlobalBounds().intersection(globalBounds)!=null)
						b.hover();				
				}
			}
			
		}
	}
}
