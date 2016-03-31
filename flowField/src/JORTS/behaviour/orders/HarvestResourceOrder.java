package JORTS.behaviour.orders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.jsfml.system.Vector2f;

import JORTS.behaviour.interactions.HarvestInteraction;
import JORTS.common.CommonFunctions;
import JORTS.gameElements.buildings.Building;
import JORTS.gameElements.buildings.resources.Resource;
import JORTS.gameElements.units.Entity;

public class HarvestResourceOrder extends SurroundBuildingOrder{
	public HarvestResourceOrder(SurroundBuildingOrder order)
	{
		super(order);
		orderType="harvestResource";
	}
	@Override
	public Vector2f interact(Entity e)
	{
		Vector2f v = new Vector2f(0,0);
		//e.harvest((Resource)b);
		e.beginHarvest((Resource)b);
		if(e.heldResources!=null&&e.heldResources.amount==e.maxResourceCapacity)
		{
			ArrayList<Building> viableBuildings = new ArrayList<Building>();
			for(Building b:e.player.getBuildings())
			{
				if(b.resourceRepository.contains(e.heldResources.resource))
				{
					viableBuildings.add(b);
				}
			}
			Building building = CommonFunctions.getNearestBuilding(e.player,e,viableBuildings);
			if(building!=null)
			{
				DepositResourcesOrder m = new DepositResourcesOrder(building.getSurroundOrder(),this);
				m.issue(e);
			}
		}
		return v;
	}
	@Override
	public void issue(Entity e)
	{
		if(entities.size()<baseOrder.openCells.size())
			super.issue(e);
		else
		{
			System.out.println();
			try{CommonFunctions.getNearestAccessibleResource(b.getPosition(),((Resource)b).buildingType).getHarvestOrder().issue(e);}
			catch(NullPointerException ex){}
		}
	}
}
