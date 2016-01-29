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
public class Player implements Drawable,Observer{ 
	public String id="";
	Color teamColor;
	public Field lastField;
	ArrayList<Entity> units;
	ArrayList<Building> buildings;
	
	ArrayList<Entity> selectedUnits;
	ArrayList<Building> selectedBuildings;
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
		id="player_"+playerNum;
		color=playerColors[playerNum];
		startPosition=spawnPoints[playerNum];		
		
		selectionPoint = new Vector2f(0,0);
		units=new ArrayList<Entity>();
		buildings=new ArrayList<Building>();
		
		selectedUnits=new ArrayList<Entity>();
		selectedBuildings=new ArrayList<Building>();
		
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
			RectangleShape selectionRect = new RectangleShape();
			selectionRect.setPosition(selectionPoint.x, selectionPoint.y);
			selectionRect.setSize(new Vector2f(v.x-selectionPoint.x+1,  v.y-selectionPoint.y+1));
			selectUnits(selectionRect.getGlobalBounds());
			
			if(selectedUnits.size()>0&&Main.mouse.doubleClick)
				selectUnitType(selectedUnits.get(0).getType());
						
			if(selectedUnits.size()==0)
			{
				selectBuildings(selectionRect.getGlobalBounds());
				if(selectedBuildings.size()>0&&Main.mouse.doubleClick)
					selectBuildingType(selectedBuildings.get(0).getType());
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
		ArrayList<Entity> temp = new ArrayList<Entity>();
		for(Entity e:units)
		{
			if(e.getType().equals(type))
				temp.add(e);
		}
		selectUnits(temp);
	}	
	public void selectBuildingType(String type)
	{
		ArrayList<Building> temp = new ArrayList<Building>();
		for(Building b:buildings)
		{
			if(b.getType().equals(type))
				temp.add(b);
		}
		selectBuildings(temp);
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
		ArrayList<Entity> temp = new ArrayList<Entity>();
		for(Entity e:units)
		{
			if(e.getControlGroup()==ctrlGrp)
				temp.add(e);		
		}
		selectUnits(temp);
	}
	public void selectUnits(FloatRect rect)
	{		
		ArrayList<Entity> temp = new ArrayList<Entity>();
		for(Entity e:units)
		{
			if(rect.contains(e.getPosition().x, e.getPosition().y))
				temp.add(e);
			else if(CommonFunctions.getDist(rect, e.getPosition())<e.getRadius())				
			{
				temp.add(e);
			}
				
		}
		selectUnits(temp);
	}
	public void selectUnits(ArrayList<Entity> es)
	{
		clearSelectedUnits();
		clearSelectedBuildings();
		for(Entity e:es)
		{
			selectedUnits.add(e);
			e.select();
		}
	}
	public void selectBuildings(ArrayList<Building> bs)
	{
		clearSelectedUnits();
		clearSelectedBuildings();
		for(Building e:bs)
		{
			selectedBuildings.add(e);
			e.select();
		}
	}
	
	public void selectUnit(Vector2f pos)
	{
		FloatRect f = new FloatRect(pos,new Vector2f(1,1));
		selectUnits(f);
	}
	public void selectBuildings(FloatRect rect)
	{		
		ArrayList<Building> temp = new ArrayList<Building>();
		for(Building b:buildings)
		{
			if(b.getGlobalBounds().intersection(rect)!=null)
				temp.add(b);				
		}
		selectBuildings(temp);
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
	private void clearSelectedBuildings() {
		for(Building b:selectedBuildings)
			b.deselect();
		selectedBuildings.clear();	
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
	
	
	public ArrayList<Entity> getUnits()
	{
		return units;
	}
	public ArrayList<Entity> getSelectedUnits()
	{
		return selectedUnits;
	}
	public ArrayList<Building> getBuildings()
	{
		return buildings;
	}
	public ArrayList<Building> getSelectedBuildings()
	{
		return selectedBuildings;
	}
}
