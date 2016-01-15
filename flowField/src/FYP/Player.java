package FYP;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;

import FYP.flowField.Field;
import FYP.flowField.InfluenceMap;
import buildings.Building;
import common.CommonFunctions;
import units.Entity;

import static FYP.Main.worldMap;
import static common.Constants.*;
;public class Player implements Drawable,Observer{ 
	String id="";
	Color teamColor;
	public Field lastField;
	ArrayList<Entity> units;
	ArrayList<Building> buildings;
	ArrayList<Entity> selectedUnits;
	public Field currentField=new Field(worldMap);
	public Vector2f selectionPoint;
	public boolean selectionInProgress=false;
	public static Color playerColors[] = new Color[]{Color.BLUE,Color.RED,Color.MAGENTA,new Color(150,0,150)};
	private static Vector2f offset = new Vector2f(200,300);
	public static Vector2f spawnPoints[] = new Vector2f[]{
			//new Vector2f(offset.x,offset.y),
			//new Vector2f(offset.x,offset.y),
		//	new Vector2f(offset.x,offset.y),
			new Vector2f(offset.x,offset.y),
			new Vector2f((CELL_SIZE*GRID_SIZE)-offset.x,(CELL_SIZE*GRID_SIZE)-offset.y),
			new Vector2f(offset.x,(CELL_SIZE*GRID_SIZE)-offset.y),
			new Vector2f((CELL_SIZE*GRID_SIZE)-offset.x,offset.y)
	};
	public static int players=0;
	public int playerNum;
	public Color color;
	public Vector2f startPosition;
	
	public Player()
	{
		playerNum=players++;
		color=playerColors[playerNum];
		startPosition=spawnPoints[playerNum];		
		
		selectionPoint = new Vector2f(0,0);
		units=new ArrayList<Entity>();
		selectedUnits=new ArrayList<Entity>();
		buildings=new ArrayList<Building>();
		Main.game.addObserver(this);
	}
	public void startSelection(Vector2f v)
	{
		selectionPoint = v;
		selectionInProgress=true;
	}
	public void endSelection(Vector2f v,boolean doSelection)
	{
		if(doSelection)
		{
			if(CommonFunctions.getDist(v, selectionPoint)>2)
			{
				RectangleShape selectionRect = new RectangleShape();
				selectionRect.setPosition(selectionPoint.x, selectionPoint.y);
				selectionRect.setSize(new Vector2f(v.x-selectionPoint.x,  v.y-selectionPoint.y));
				selectUnits(selectionRect.getGlobalBounds());
			}
			else
			{
				selectUnit(v);
				if(selectedUnits.size()>0&&Main.mouse.doubleClick)
					selectUnitType(selectedUnits.get(0).getType());
			}
		}
		selectionInProgress=false;
	}
	public void addUnit(Entity e)
	{
		units.add(e);
		e.setPlayer(this);
	}
	public void addBuilding(Building e)
	{
		buildings.add(e);
		e.setPlayer(this);
	}
	public void selectUnitType(String type)
	{
		for(Entity e:units)
		{
			if(e.getType().equals(type))
				selectUnit(e);
		}
	}
	public void assignControlGroup(int ctrlGrp)
	{
		for(Entity e:selectedUnits)
		{
			if(e.getControlGroup()==ctrlGrp)
				e.setControlGroup(-1);
			e.setControlGroup(ctrlGrp);
		}
	}
	public void selectControlGroup(int ctrlGrp)
	{
		clearSelectedUnits();
		for(Entity e:units)
		{
			if(e.getControlGroup()==ctrlGrp)
				selectUnit(e);				
		}
	}
	public void selectUnits(FloatRect rect)
	{
		clearSelectedUnits();
		for(Entity e:units)
		{
			if(rect.contains(e.getPosition().x, e.getPosition().y))
				selectUnit(e);
			else if(CommonFunctions.getDist(rect, e.getPosition())<e.getRadius())				
			{
				selectUnit(e);
			}
				
		}
	}
	public void selectUnits(ArrayList<Entity> es)
	{
		for(Entity e:es)
			selectUnit(e);
	}
	
	public void selectUnit(Entity e)
	{
		selectedUnits.add(e);
		e.select();
	}
	public void selectUnit(Vector2f pos)
	{

		clearSelectedUnits();
		for(Entity e:units)
		{
			if(CommonFunctions.getDist(pos, e.getPosition())<e.getRadius())
			{
				selectUnit(e);
				return;
			}
		}
	}
	public void deselectUnit(Entity e)
	{
		selectedUnits.remove(e);
		e.select();
	}
	public void clearSelectedUnits()
	{
		for(Entity e:selectedUnits)
			e.deselect();
		selectedUnits.clear();
	}
	public void issueMoveCommand(Vector2f loc)
	{
		if(selectedUnits.size()>0)
		{
			MoveOrder m = new MoveOrder();
			m.init(loc, selectedUnits.size()/2);
			currentField=m.flowField;
			for(Entity e:selectedUnits)
			{
				e.currentOrder = m;
			}
		}
	}
	public void issueFollowCommand(Entity ent)
	{
		if(selectedUnits.size()>0)
		{
			FollowOrder m = new FollowOrder();
			m.init(ent);
			currentField=m.flowField;
			for(Entity e:selectedUnits)
			{
				e.currentOrder = m;
			}
		}
	}
	public ArrayList<Entity> getUnits()
	{
		return units;
	}
	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) {
		for(Entity e:units)
		{
			arg0.draw(e);
		}
		for(Building b:buildings)
		{
			arg0.draw(b);
		}
	}
	public void tick()
	{
		for(Entity entity:units)
		   entity.tick();
		for(Entity entity:units)
		   entity.reregister();
	}
	@Override
	public void update(Observable o, Object arg) {
		tick();		
	}
	public ArrayList<Entity> getSelectedUnits()
	{
		return selectedUnits;
	}
}
