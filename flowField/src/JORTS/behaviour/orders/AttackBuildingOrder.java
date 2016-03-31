package JORTS.behaviour.orders;

import org.jsfml.system.Vector2f;

import JORTS.gameElements.units.Entity;

public class AttackBuildingOrder extends SurroundBuildingOrder {
	public AttackBuildingOrder(SurroundBuildingOrder order)
	{
		super(order);
		orderType="attackBuilding";
	}
	@Override
	public Vector2f interact(Entity e) {
		Vector2f v = new Vector2f(0,0);
		e.attack(b);
		return v;
	}
}
