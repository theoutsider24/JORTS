package JORTS.behaviour.orders;

import org.jsfml.system.Vector2f;

import JORTS.behaviour.flowField.Field;
import JORTS.core.Main;

public class MoveOrder extends Order {
	public MoveOrder()
	{
		super();
		flowField = new Field(Main.worldMap);
	}
	public void init(Vector2f loc,int size,float minX,float maxX, float minY,float maxY)
	{
		t = new Thread(new Runnable(){
			@Override
			public void run() {
				flowField.openCellatPos(loc,size,minX,maxX,minY,maxY);			
				initialised=true;
			}
		},"Move_Order_Thread");
		t.start();	
	}
}
