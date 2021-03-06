package JORTS.gameElements.map;

import static JORTS.common.Constants.CELL_SIZE;

import java.util.ArrayList;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.system.Vector2f;

import JORTS.gameElements.buildings.Building;
import JORTS.gameElements.units.Entity;

public class MapCell extends RectangleShape{
	boolean traversable=true;
	public TerrainDefinition terrain;
	public int cost=1;
	public int x=0;
	public int y=0;
	int perUnitCost=2;
	ArrayList<Entity> entities;
	ArrayList<Building> buildings;
	public Color color=new Color(0,0,0);
	Color highlightColor=new Color(0,0,0);
	public boolean highlighted=false;
	public void highlight(Color c)
	{
		highlighted=true;
		highlightColor=c;
		setFillColor(highlightColor);
	}
	public void unhighlight()
	{
		highlighted=false;
		highlightColor=new Color(0,0,0);
		setFillColor(color);
	}	
	
	public MapCell(int x,int y)
	{
		super();
		this.x=x;
		this.y=y;
		setPosition(x*CELL_SIZE, y*CELL_SIZE);
		setSize(new Vector2f(CELL_SIZE,CELL_SIZE));
		entities=new ArrayList<Entity>();
		buildings=new ArrayList<Building>();
		//setFillColor(Color.add(c));
	}
	
	public boolean isTraversable()
	{
		return traversable;
	}
	public void open()
	{
		traversable=true;
		cost=1;
		//setFillColor(Color.add(color,highlightColor));
		/*if(texture!=null)
		{
			setTexture(texture);
			setFillColor(Color.WHITE);
		}*/
	}
	public void close()
	{
		traversable=false;
		cost=0;
		//setFillColor(Color.add(OBSTACLE_COLOR,highlightColor));
	}
	public void setCost(int cost)
	{
		if(cost==0)
			close();
		else
			open();
		this.cost=cost;	
	}
	public void increaseCost(int c)
	{
		cost=cost+c;
	}
	public int getCost()
	{
		return cost;
	}
	
	/*public Color getDrawColor()
	{
		//if(mask) return new Color(0,0,0,50);

		//if(!visible) return new Color(20,20,20);
		if(traversable) return Color.add(GROUND_COLOR,highlightColor);
		else return Color.add(OBSTACLE_COLOR,highlightColor);
	}
	*/
	public void registerUnit(Entity e)
	{
		if(!entities.contains(e))
		{
		//	if(cost!=0)cost+=perUnitCost;
			entities.add(e);
		}
	}
	public void registerBuilding(Building b)
	{
		if(!buildings.contains(b))
		{
			buildings.add(b);
		}
	}
	public void deregisterUnit(Entity e)
	{
		if(entities.contains(e))
		{
			//if(cost>1)cost-=perUnitCost;
			entities.remove(e);
		}
	}	
	public void deregisterBuilding(Building b)
	{
		if(buildings.contains(b))
		{
			buildings.remove(b);
		}
	}
	public void clearUnits()
	{
		entities.clear();
	}
	public ArrayList<Entity> getEntities()
	{
		return entities;
	}
	public ArrayList<Building> getBuildings()
	{
		return buildings;
	}
	public Vector2f getCenter()
	{
		return Vector2f.add(getPosition(), new Vector2f(CELL_SIZE/2,CELL_SIZE/2));
	}
}
