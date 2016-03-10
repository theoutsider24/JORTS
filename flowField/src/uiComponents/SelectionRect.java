package uiComponents;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import FYP.GameWindow;
import FYP.Main;
import common.CommonFunctions;
import gameElements.buildings.Building;
import gameElements.units.Entity;

public class SelectionRect extends RectangleShape{
	boolean selectionInProgress=false;
	
	public RectangleShape realRect = new RectangleShape();
	public FloatRect globalBounds= realRect.getGlobalBounds();
	public GameWindow window;
	public SelectionRect(GameWindow window)
	{
		this.window=window;
		setFillColor(Color.TRANSPARENT);
		setOutlineColor(Color.WHITE);
		setOutlineThickness(1);
	}
	public void start(Vector2f v)
	{
		setPosition(v);
		window.setView(window.gameView);	
		realRect.setPosition(window.mapPixelToCoords(new Vector2i((int)v.x,(int)v.y)));
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
			window.setView(window.gameView);	
			Vector2f vReal= window.mapPixelToCoords(new Vector2i((int)v.x,(int)v.y));
			realRect.setPosition(realRect.getPosition());
			realRect.setSize(Vector2f.sub(vReal, realRect.getPosition()));
			globalBounds= realRect.getGlobalBounds();

			//RectangleShape r=new RectangleShape();
			//FloatRect rect=realRect.getGlobalBounds();
			boolean hoverUnits=false;
			for(Entity e:window.activePlayer.getUnits())
			{
				if(globalBounds.contains(e.getPosition().x, e.getPosition().y)||CommonFunctions.getDist(globalBounds, e.getPosition())<e.getRadius())
				{
					e.hover();
					hoverUnits=true;
				}
			}
			if(!hoverUnits)
			{
				for(Building b:window.activePlayer.getBuildings())
				{
					if(b.getGlobalBounds().intersection(globalBounds)!=null)
						b.hover();				
				}
			}
			
		}
	}
}
