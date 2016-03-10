package gameElements.buildings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import static common.Constants.*;

import FYP.Main;
import FYP.Player;
import behaviour.abilities.Ability;
import behaviour.abilities.CreateUnitAbility;
import behaviour.orders.SurroundBuildingOrder;
import behaviour.timedBehaviours.TimedBehaviour;
import gameElements.map.MapCell;
import gameElements.units.Entity;
import gameElements.units.UnitFactory;

public abstract class Building extends RectangleShape {
	public static HashMap<String,Building> allBuildings = new HashMap<String,Building>();
	public ArrayList<int[]> offsets = new ArrayList<int[]>(); 
	public boolean placed=false;
	public boolean valid=true;
	
	public int[] origin=new int[2];
	public int maxQueueSize=6;
	public ArrayList<MapCell> occupiedCells = new ArrayList<MapCell>();
	public ArrayList<Ability> abilities = new ArrayList<Ability>();
	//public ArrayList<CreateUnitAbility> productionQueue;
	public ArrayList<TimedBehaviour> productionQueue;
	public String id;
	public static int numberOfBuildings;
	public String buildingType="building";
	public Player player;
	Color playerColor=new Color(50,50,50,0);
	boolean selected=false;
	public SurroundBuildingOrder surroundOrder;
	int outlineThickness=5;
	
	public Building()
	{
		id=buildingType+"_#"+numberOfBuildings++;
		
		offsets.add(new int[]{0,0});		
		offsets.add(new int[]{-1,-1});
		offsets.add(new int[]{0,-1});
		offsets.add(new int[]{-1,0});		
		setFillColor(playerColor);
		setSize(new Vector2f((2*CELL_SIZE)-outlineThickness*2,(2*CELL_SIZE)-outlineThickness*2));
		
		productionQueue=new ArrayList<TimedBehaviour>();
		
		
		//surroundOrder= new SurroundBuildingOrder();
	}
	public void cancelProduction(int i)
	{
		try{
		productionQueue.get(i).stop();
		productionQueue.remove(i);}
		catch(Exception ex){}
	}
	public void tick()
	{
		if(productionQueue.size()>0&&!productionQueue.get(0).running)
		{
			if(productionQueue.get(0).timeToComplete<=0)productionQueue.remove(0);
			if(productionQueue.size()>0)
				productionQueue.get(0).start();
		}
		
	}
	public void startProduction(String type,int time)
	{
		if(productionQueue.size()<maxQueueSize)
		{
			productionQueue.add(
				new TimedBehaviour(time)
				{
					@Override
					public void run(){
						Vector2i spawnLoc = getSpawnLocation(); 
						Entity e=UnitFactory.buildEntity(type,spawnLoc.x,spawnLoc.y,player);
						if(e!=null)
							player.addUnit(e);
				}}
			);
		}
	}
	public void place()
	{
		setFillColor(playerColor);
		setOutlineColor(playerColor);
		setOutlineThickness(outlineThickness);
		
		//System.out.println(origin[0]);
		//System.out.println(origin[1]);
		//System.out.println(Main.worldMap.getCell(origin[0],origin[1]).getPosition().x);
		setPosition(Main.worldMap.getCell(origin[0]-1, origin[1]-1).getPosition());
		setOrigin(Vector2f.div(getSize(), 2));
		move(Vector2f.div(getSize(), 2));
		move(outlineThickness,outlineThickness);
		for(int[] offset:offsets)
		{
			//Main.worldMap.closeCell(origin[0]+offset[0], origin[1]+offset[1]);
			MapCell c = Main.worldMap.getCell(origin[0]+offset[0], origin[1]+offset[1]);
			occupiedCells.add(c);
			c.close();
			c.registerBuilding(this);
		}
		Main.worldMap.refreshImage();
		allBuildings.put(id,this);

		surroundOrder = new SurroundBuildingOrder();
		surroundOrder.init(this);
	}
	public void setPlayer(Player p)
	{
		player=p;
		playerColor=p.color;
	}
	@Override
	public void draw(RenderTarget arg0, RenderStates arg1)
	{
		super.draw(arg0, arg1);
		setOutlineColor(playerColor);
		if(selected)
			setOutlineColor(Color.WHITE);
	}	
	public void hover()
	{
		if(!selected)setOutlineColor(Color.add(new Color(200,200,200),playerColor));
	}
	public void select()
	{
		selected=true;
	}
	public void deselect()
	{
		selected=false;
	}
	public String getType()
	{
		return buildingType;
	}
	public Vector2i getSpawnLocation()
	{
		Vector2f spawnCenter = Vector2f.add(this.getPosition(),Vector2f.div(this.getSize(),2));
		float halfWidth=this.getSize().x/2;
		float halfHeight=this.getSize().y/2;
		return new Vector2i((int)(spawnCenter.x+halfWidth),(int)(spawnCenter.y+halfHeight));		
	}
}
