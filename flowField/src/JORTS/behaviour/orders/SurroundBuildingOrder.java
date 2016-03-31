package JORTS.behaviour.orders;

import java.util.ArrayList;

import org.jsfml.system.Vector2f;

import JORTS.behaviour.flowField.Field;
import JORTS.behaviour.flowField.FlowCell;
import JORTS.common.CommonFunctions;
import JORTS.core.Main;
import JORTS.gameElements.buildings.Building;
import JORTS.gameElements.map.MapCell;
import JORTS.gameElements.units.Entity;

public class SurroundBuildingOrder extends Order{
	public Building b;
	public ArrayList<MapCell> openCells;
	public SurroundBuildingOrder baseOrder;
	public SurroundBuildingOrder()
	{
		super();
		orderType="surroundBuilding";
		flowField = new Field(Main.worldMap);
		openCells=new ArrayList<MapCell>();
	}
	public SurroundBuildingOrder(SurroundBuildingOrder order)
	{
		this();
		this.baseOrder=order;
		this.b=baseOrder.b;
		flowField=baseOrder.flowField;
	}
	public void init(Building b)
	{
		this.b=b;
		openCells=new ArrayList<MapCell>();
		for(MapCell c:b.occupiedCells)
		{
			for(int i=-1;i<=1;i++)
				for(int j=-1;j<=1;j++)
				{
					if(i==0||j==0)//stops corners from being added
						if(Main.worldMap.isCellTraversable(c.x+i, c.y+j))
							if(!openCells.contains(Main.worldMap.getCell(c.x+i, c.y+j)))
								openCells.add(Main.worldMap.getCell(c.x+i, c.y+j));
				}
		}
		ArrayList<Vector2f> list = new ArrayList<Vector2f>();
		for(MapCell c:openCells)
		{
			list.add(new Vector2f(c.x,c.y));
		}
		//flowField.openCellLocations(list);
		
		t = new Thread(new Runnable(){
			@Override
			public void run() {
				flowField.openCellLocations(list);		
				initialised=true;
			}
		},"Surround_Building_Order_Thread");
		t.start();	
	}
	@Override
	public Vector2f getVector(Entity e)
	{
		if(b.destroyed)
		{
			Order.IdleOrder.issue(e);
		}
		Vector2f v= super.getVector(e);

		int dist=(int) b.getDistance(e);
		
		if(dist<e.attackRange)
			v=interact(e);
		else if(CommonFunctions.getLength(v)==0)
		{
			v=Vector2f.sub(CommonFunctions.getClosestPoint(b.getGlobalBounds(), e.getPosition()), e.getPosition());
			v=CommonFunctions.normaliseVector(v);
			v=Vector2f.mul(v, FlowCell.maxSpeed);		
		}
		return v;
	}
	public Vector2f interact(Entity e)
	{
		return new Vector2f(0,0);
	}
}
