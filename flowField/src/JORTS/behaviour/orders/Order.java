package JORTS.behaviour.orders;

import java.util.ArrayList;

import org.jsfml.system.Vector2f;

import JORTS.behaviour.flowField.Field;
import JORTS.common.CommonFunctions;
import JORTS.core.Main;
import JORTS.gameElements.units.Entity;

public abstract class Order {
	Thread t;
	public String orderType="";
	public Field flowField;
	//ArrayList<Entity> targets;
	public static final Order IdleOrder=new Order(){};
	public ArrayList<Entity> entities;
	public boolean initialised=false;
	public Order()
	{
		entities = new ArrayList<Entity>();
		flowField = Field.nullField;//new Field(Main.worldMap);
		//targets = new ArrayList<Entity>();
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
		}
	}
	public Vector2f getVector(Entity e)
	{
		Vector2f v= flowField.getFlowAtPos(e.getPosition());
		if(v.equals(Vector2f.ZERO)&&flowField!=Field.nullField&&initialised&&flowField.getCellAtPos(e.getPosition()).integration==255&&flowField.getCellAtPos(e.getPosition()).isOpen())
		{
			Order.IdleOrder.issue(e);
			try{Main.getPlayerWindow(e.player).setErrorMessage("Cannot Reach Goal");}
			catch(Exception ex){}
			System.out.println("error");
		}
		
		float length =  CommonFunctions.getLength(v);
		Vector2f v2 = Vector2f.sub(flowField.getCellAtPos(e.getPosition()).getCenter(), e.getPosition());
		v2=CommonFunctions.limitVectorLength(v2, length/4);
		
		
		Vector2f v3 = Vector2f.add(v, v2);
		/*if(CommonFunctions.getLength(v3)>length)
			v3=CommonFunctions.limitVectorLength(v3, length);
		else
		{
			v3=
		}*/
		v3=CommonFunctions.normaliseVector(v3);
		//v3=Vector2f.mul(v3,length);
		
		
		return v3;
	}
}
