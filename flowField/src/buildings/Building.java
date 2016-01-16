package buildings;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;

import static common.Constants.*;
import FYP.Main;
import FYP.Player;
import map.MapCell;
import units.Entity;

public class Building extends RectangleShape{
	public static HashMap<String,Building> allBuildings = new HashMap<String,Building>();
	public ArrayList<int[]> offsets = new ArrayList<int[]>(); 
	public boolean placed=false;
	public int[] origin=new int[2];
	
	public String id;
	public static int numberOfBuildings;
	
	public Player player;
	Color playerColor=new Color(50,50,50,0);
	
	int outlineThickness=5;
	
	public Building()
	{
		id="building#"+numberOfBuildings++;
		
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
	}	
	public void hover()
	{
		setOutlineColor(new Color(100,100,255));
	}
}
