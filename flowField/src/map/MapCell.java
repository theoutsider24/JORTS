package map;

import java.util.ArrayList;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import static common.Constants.*;
import units.Entity;

public class MapCell extends RectangleShape{
	boolean traversable=true;
	public int cost=1;
	public int x=0;
	public int y=0;
	int perUnitCost=2;
	ArrayList<Entity> entities;
	
	public MapCell(int x,int y)
	{
		super();
		this.x=x;
		this.y=y;
		entities=new ArrayList<Entity>();
	}
	public MapCell(int cost,int x,int y)
	{
		super();
		this.x=x;
		this.y=y;
		entities=new ArrayList<Entity>();
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
		if(traversable) return GROUND_COLOR;
		else return OBSTACLE_COLOR;
	}
	
	public void registerUnit(Entity e)
	{
		if(!entities.contains(e))
		{
		//	if(cost!=0)cost+=perUnitCost;
			entities.add(e);
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
	public void clearUnits()
	{
		entities.clear();
	}
	public ArrayList<Entity> getEntities()
	{
		return entities;
	}
}
