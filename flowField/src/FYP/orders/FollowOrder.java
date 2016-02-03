package FYP.orders;

import java.util.Observable;
import java.util.Observer;

import org.jsfml.system.Vector2f;

import FYP.Main;
import units.Entity;

public class FollowOrder extends Order implements Observer{
	public FollowOrder()
	{
		super();
	}
	public void init(Entity e)
	{
		targets.add(e);
		flowField.openCellatPos(e.getPosition(),1);
		Main.game.addObserver(this);
	}
	public void update(Observable o, Object arg)
	{
		boolean thread=true;
		
		if(thread)
		{
			if(Main.worldMap.getCellAtPos(flowField.goalPosition)!=
					Main.worldMap.getCellAtPos(targets.get(0).getPosition()))
			{
				Thread t = new Thread(new Runnable(){
					@Override
					public void run() {
						flowField.openCellatPos(targets.get(0).getPosition(),1);					
					}
				});
				t.start();	
			}
		}
		else
		{
			if(Main.worldMap.getCellAtPos(flowField.goalPosition)!=
			   Main.worldMap.getCellAtPos(targets.get(0).getPosition()))
				flowField.openCellatPos(targets.get(0).getPosition(),1);	
		}
	}
}
