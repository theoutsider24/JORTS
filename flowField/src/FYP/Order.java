package FYP;

import java.util.ArrayList;

import org.jsfml.graphics.CircleShape;
import org.jsfml.graphics.RectangleShape;

import FYP.flowField.Field;

public abstract class Order {
	public Field flowField;
	ArrayList<CircleShape> targets;
	public static final Order IdleOrder=new Order(){};
	public Order()
	{
		flowField = Field.nullField;//new Field(Main.worldMap);
		targets = new ArrayList<CircleShape>();
	}
	
}
