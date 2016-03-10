package behaviour.orders;

import java.util.ArrayList;

import org.jsfml.system.Vector2f;

import FYP.Main;
import behaviour.flowField.Field;
import gameElements.units.Entity;

public class MoveOrder extends Order {
	public MoveOrder()
	{
		super();
		flowField = new Field(Main.worldMap);
	}
	public void init(Vector2f loc,int size,float minX,float maxX, float minY,float maxY)
	{
		//flowField.openCellatPos(loc,size,minX,maxX,minY,maxY);
		t = new Thread(new Runnable(){
			@Override
			public void run() {
				System.out.println(minX+","+minY + " : " + maxX + ","+maxY);
				flowField.openCellatPos(loc,size,minX,maxX,minY,maxY);			
			}
		});
		t.start();	
	}
}
