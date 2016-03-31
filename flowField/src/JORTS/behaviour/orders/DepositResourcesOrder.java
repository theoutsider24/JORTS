package JORTS.behaviour.orders;

import org.jsfml.system.Vector2f;

import JORTS.gameElements.buildings.resources.Resource;
import JORTS.gameElements.units.Entity;

public class DepositResourcesOrder extends SurroundBuildingOrder{
	HarvestResourceOrder previousOrder;
	public DepositResourcesOrder(SurroundBuildingOrder order)
	{
		super(order);
		orderType="depositResource";
	}
	public DepositResourcesOrder(SurroundBuildingOrder order,HarvestResourceOrder previousOrder)
	{
		super(order);
		this.previousOrder=previousOrder;
		orderType="depositResource";
	}
	@Override
	public Vector2f interact(Entity e) {
		Vector2f v = new Vector2f(0,0);
		e.player.collectResource(e.heldResources.resource,e.heldResources.amount);
		e.heldResources=null;
		if(previousOrder!=null)
			previousOrder.issue(e);
		else
			Order.IdleOrder.issue(e);
		return v;
	}
}
