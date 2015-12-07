package FYP;

import org.jsfml.system.Vector2f;

import FYP.flowField.Field;

public class MoveOrder extends Order {
	public MoveOrder()
	{
		super();	
	}
	public void init(Vector2f loc,int size)
	{
		flowField.openCellatPos(loc,size);
	}
}
