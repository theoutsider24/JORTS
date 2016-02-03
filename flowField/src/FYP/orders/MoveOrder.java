package FYP.orders;

import java.util.ArrayList;

import org.jsfml.system.Vector2f;

import FYP.Main;
import FYP.flowField.Field;

public class MoveOrder extends Order {
	public MoveOrder()
	{
		super();	
		flowField = new Field(Main.worldMap);
	}
	public void init(Vector2f loc,int size)
	{
		flowField.openCellatPos(loc,size);
	}
}
