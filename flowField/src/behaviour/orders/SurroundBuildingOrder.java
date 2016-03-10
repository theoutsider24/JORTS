package behaviour.orders;

import java.util.ArrayList;

import org.jsfml.system.Vector2f;

import FYP.Main;
import behaviour.flowField.Field;
import behaviour.flowField.FlowCell;
import common.CommonFunctions;
import gameElements.buildings.Building;
import gameElements.map.MapCell;
import gameElements.units.Entity;

public class SurroundBuildingOrder extends Order{
	public Building b;
	public SurroundBuildingOrder()
	{
		super();
		orderType="attackBuilding";
		flowField = new Field(Main.worldMap);
	}
	public void init(Building b)
	{
		this.b=b;
		ArrayList<MapCell> cells=new ArrayList<MapCell>();
		for(MapCell c:b.occupiedCells)
		{
			for(int i=-1;i<=1;i++)
				for(int j=-1;j<=1;j++)
				{
					if(Main.worldMap.isCellTraversable(c.x+i, c.y+j))
						if(!cells.contains(Main.worldMap.getCell(c.x+i, c.y+j)))
							cells.add(Main.worldMap.getCell(c.x+i, c.y+j));
				}
		}
		ArrayList<Vector2f> list = new ArrayList<Vector2f>();
		for(MapCell c:cells)
		{
			list.add(new Vector2f(c.x,c.y));
		}
		//flowField.openCellLocations(list);
		
		t = new Thread(new Runnable(){
			@Override
			public void run() {
				flowField.openCellLocations(list);			
			}
		});
		t.start();	
	}
	@Override
	public Vector2f getVector(Entity e)
	{
		Vector2f v= super.getVector(e);
		if(CommonFunctions.getLength(v)==0&&flowField.calculated)
		{
			//System.out.println(CommonFunctions.getDist(b.getGlobalBounds(),e.getPosition())-e.getRadius());
			if(!(CommonFunctions.getDist(b.getGlobalBounds(),e.getPosition())-e.getRadius()<30))
			{				
				v=Vector2f.sub(b.getPosition(), e.getPosition());
				v=CommonFunctions.normaliseVector(v);
				v=Vector2f.mul(v, FlowCell.maxSpeed);
			}
		}
		//if(flowField.calculated)System.out.println(v.x+","+v.y);
		return v;
	}
}
