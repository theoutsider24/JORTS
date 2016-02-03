package map;

import java.util.ArrayList;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;

import buildings.Building;

import static common.Constants.*;
import units.Entity;

public class MapCell extends RectangleShape{
	boolean traversable=true;
	public int cost=1;
	public int x=0;
	public int y=0;
	int perUnitCost=2;
	ArrayList<Entity> entities;
	ArrayList<Building> buildings;
	Color highlightColor=new Color(0,0,0);
	public boolean visible=false;
	public boolean mask=false;
	
	public void highlight(Color c)
	{
		highlightColor=c;
	}
	public void unhighlight()
	{
		highlightColor=new Color(0,0,0);
	}	
	
	public MapCell(int x,int y)
	{
		super();
		this.x=x;
		this.y=y;
		setPosition(x*CELL_SIZE, y*CELL_SIZE);
		entities=new ArrayList<Entity>();
		buildings=new ArrayList<Building>();
	}
	public MapCell(int cost,int x,int y)
	{
		super();
		this.x=x;
		this.y=y;
		entities=new ArrayList<Entity>();
		buildings=new ArrayList<Building>();
		if(cost==0)
			close();
		this.cost=cost;				
	}
	
	public boolean isTraversable()
	{
		return traversable;
	}
	public void open()
	{
		traversable=true;
		cost=1;
	}
	public void close()
	{
		traversable=false;
		cost=0;
	}
	public void setCost(int cost)
	{
		if(cost==0)
			close();
		else
			open();
		this.cost=cost;	
	}
	public int getCost()
	{
		return cost;
	}
	
	public Color getDrawColor()
	{
		//if(mask) return new Color(0,0,0,50);

		//if(!visible) return new Color(20,20,20);
		if(traversable) return Color.add(GROUND_COLOR,highlightColor);
		else return Color.add(OBSTACLE_COLOR,highlightColor);
	}
	
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
}
