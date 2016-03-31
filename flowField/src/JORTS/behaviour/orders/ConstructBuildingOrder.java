package JORTS.behaviour.orders;

import org.jsfml.system.Vector2f;

import JORTS.gameElements.units.Entity;

public class ConstructBuildingOrder extends SurroundBuildingOrder {
	public ConstructBuildingOrder(SurroundBuildingOrder order)
	{
		super(order);
		orderType="attackBuilding";
	}
	@Override
	public Vector2f interact(Entity e) {

		Vector2f v = new Vector2f(0,0);
		if(!b.inConstruction)
		{
			Order.IdleOrder.issue(e);
		}
		else
		{
			e.attack(b);
		}
		return v;
	}
}
