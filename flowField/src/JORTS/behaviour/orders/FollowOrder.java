package JORTS.behaviour.orders;

import java.util.Observable;
import java.util.Observer;

import org.jsfml.system.Vector2f;

import JORTS.behaviour.flowField.Field;
import JORTS.behaviour.flowField.FlowCell;
import JORTS.common.CommonFunctions;
import JORTS.core.Main;
import JORTS.core.Player;
import JORTS.gameElements.units.Entity;

public class FollowOrder extends Order implements Observer{
	float minX,maxX,minY,maxY;
	Vector2f loc;
	Player targetPlayer;
	Entity target;
	public FollowOrder()
	{
		super();
		flowField = new Field(Main.worldMap);
	}
	public void init(Entity e)
	{
		target=e;
		targetPlayer=e.player;
		Main.game.addObserver(this);
	}
	public void update(Observable o, Object arg)
	{
		if(entities.isEmpty())
		{
			Main.game.deleteObserver(this);
			return;
		}
		if(target==null||target.dead)
		{
			target=null;
			for(int i=0;i<entities.size();i++)
				getVector(entities.get(i));
		}
		else if(CommonFunctions.isVisible(target, entities.get(0).player))
		{
			boolean thread=true;
			loc =target.getPosition();
			
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
							System.out.println("update"+loc+", "+target.id);
							flowField.openCellatPos(loc,1,minX,maxX,minY,maxY);		
							
							initialised=true;
						}
					},"Follow_Order_Update_Thread");
					initialised=false;
					t.start();	
				}
			}
			else
			{
				if(Main.worldMap.getCellAtPos(flowField.goalPosition)!=
				   Main.worldMap.getCellAtPos(target.getPosition()))
					flowField.openCellatPos(target.getPosition());	
			}
		}
	}
	@Override
	public Vector2f getVector(Entity e)
	{
		Vector2f v= super.getVector(e);
		if(target==null||target.dead)
		{
			FollowOrder o = new FollowOrder();
			Entity entity = CommonFunctions.getNearestEntity(targetPlayer, e,500);//500 is search range
			if(entity!=null)
			{
				if(entity.followOrder!=null)
				{
					entity.followOrder.issue(e);
				}
				else
				{
					entity.followOrder=o;
					o.init(entity);
					o.issue(e);
				}
			}
			else
				Order.IdleOrder.issue(e);
		}
		else
		{				
			int dist=(int)e.getDistance(target);
			
			if(dist<e.attackRange)
			{		
				v=new Vector2f(0,0);
				e.attack(target);
			}
			else if(CommonFunctions.getLength(v)==0)
			{
				v=Vector2f.sub(target.getPosition(), e.getPosition());
				v=CommonFunctions.normaliseVector(v);
				v=Vector2f.mul(v, FlowCell.maxSpeed);		
			}
		}
		return v;
	}
}
