package behaviour.orders;

import java.util.ArrayList;

import org.jsfml.graphics.CircleShape;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.system.Vector2f;

import FYP.Main;
import behaviour.flowField.Field;
import gameElements.units.Entity;

public abstract class Order {
	Thread t;
	public String orderType="";
	public Field flowField;
	ArrayList<Entity> targets;
	public static final Order IdleOrder=new Order(){};
	public ArrayList<Entity> entities;
	public Order()
	{
		entities = new ArrayList<Entity>();
		flowField = Field.nullField;//new Field(Main.worldMap);
		targets = new ArrayList<Entity>();
	}
	public void issue(Entity e)
	{
		if(e.currentOrder!=null&&e.currentOrder!=Order.IdleOrder)
			e.currentOrder.remove(e);
		entities.add(e);
		e.currentOrder=this;
		e.acting=false;
	}
	public void remove(Entity e)
	{
		entities.remove(e);
		if(entities.size()==0)
		{
			Main.worldMap.deleteObserver(flowField);
			/*if(t.isAlive())
			{
				System.out.println("killing");
				t.stop();
			}*/
		}
	}
	public Vector2f getVector(Entity e)
	{
		return flowField.getFlowAtPos(e.getPosition());
	}
}
