package FYP.orders;

import java.util.ArrayList;

import org.jsfml.system.Vector2f;

import FYP.Main;
import FYP.flowField.FlowCell;
import buildings.Building;
import map.MapCell;

public class AttackBuildingOrder extends Order{
	Building b;
	public AttackBuildingOrder()
	{
		super();
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
		flowField.openCellLocations(list);
	}
}
