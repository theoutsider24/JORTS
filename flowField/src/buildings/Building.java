package buildings;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import static common.Constants.*;

import FYP.Ability;
import FYP.Main;
import FYP.Player;
import map.MapCell;
import units.Entity;

public abstract class Building extends RectangleShape{
	public static HashMap<String,Building> allBuildings = new HashMap<String,Building>();
	public ArrayList<int[]> offsets = new ArrayList<int[]>(); 
	public boolean placed=false;
	public boolean valid=true;
	
	public int[] origin=new int[2];
	
	public ArrayList<MapCell> occupiedCells = new ArrayList<MapCell>();
	public ArrayList<Ability> abilities = new ArrayList<Ability>();
	
	public String id;
	public static int numberOfBuildings;
	public String buildingType="building";
	public Player player;
	Color playerColor=new Color(50,50,50,0);
	boolean selected=false;
	
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
	}
	public void place()
	{
		setFillColor(playerColor);
		setOutlineColor(playerColor);
		setOutlineThickness(outlineThickness);
		
		System.out.println(origin[0]);
		System.out.println(origin[1]);
		System.out.println(Main.worldMap.getCell(origin[0],origin[1]).getPosition().x);
		setPosition(Main.worldMap.getCell(origin[0]-1, origin[1]-1).getPosition());
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
