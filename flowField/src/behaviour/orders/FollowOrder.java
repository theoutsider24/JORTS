package behaviour.orders;

import java.util.Observable;
import java.util.Observer;

import org.jsfml.system.Vector2f;

import FYP.Main;
import behaviour.flowField.Field;
import behaviour.flowField.FlowCell;
import behaviour.interactions.UnitCombatInteraction;
import common.CommonFunctions;
import gameElements.units.Entity;

public class FollowOrder extends Order implements Observer{
	float minX,maxX,minY,maxY;
	Vector2f loc;
	public FollowOrder()
	{
		super();
		flowField = new Field(Main.worldMap);
	}
	public void init(Entity e)
	{
		targets.add(e);
		//flowField.openCellatPos(e.getPosition(),1);
		Main.game.addObserver(this);
	}
	public void update(Observable o, Object arg)
	{
		boolean thread=true;
		loc =targets.get(0).getPosition();
		
		
		minX=loc.x;
		maxX=loc.x;
		
		minY=loc.y;
		maxY=loc.y;
		for(Entity e:entities)
		{
			if(e.getPosition().x>maxX)
				maxX=e.getPosition().x;
			if(e.getPosition().x<minX)
				minX=e.getPosition().x;
			
			if(e.getPosition().y>maxY)
				maxY=e.getPosition().y;
			if(e.getPosition().y<minY)
				minY=e.getPosition().y;
		}
		
		if(thread)
		{
			if(Main.worldMap.getCellAtPos(flowField.goalPosition)!=
					Main.worldMap.getCellAtPos(loc))
			{
				Thread t = new Thread(new Runnable(){
					@Override
					public void run() {
						//System.out.println(minX+","+minY + " : " + maxX + ","+maxY);
						flowField.openCellatPos(loc,1,minX,maxX,minY,maxY);					
					}
				});
				t.start();	
			}
		}
		else
		{
			if(Main.worldMap.getCellAtPos(flowField.goalPosition)!=
			   Main.worldMap.getCellAtPos(targets.get(0).getPosition()))
				flowField.openCellatPos(targets.get(0).getPosition());	
		}
	}
	@Override
	public Vector2f getVector(Entity e)
	{
		if(targets.get(0).dead)
		{
			Order.IdleOrder.issue(e);
		}
		Entity target=targets.get(0);
		Vector2f v= super.getVector(e);
			
		int dist=(int) (CommonFunctions.getDist(target.getPosition(),e.getPosition())-e.getRadius()-target.getRadius());
		if(dist<50)
		{				
			if(dist>e.influenceRange)
			{
				v=Vector2f.sub(target.getPosition(), e.getPosition());
				v=CommonFunctions.normaliseVector(v);
				v=Vector2f.mul(v, FlowCell.maxSpeed);
			}
				
		//	System.out.println("attacking");
			//v=Vector2f.sub(target.getPosition(), e.getPosition());
			//v=CommonFunctions.normaliseVector(v);
			//v=Vector2f.mul(v, FlowCell.maxSpeed);
			if(!e.acting)new UnitCombatInteraction(1000,e,target);
		}
		
		//if(flowField.calculated)System.out.println(v.x+","+v.y);
		return v;
	}
}
