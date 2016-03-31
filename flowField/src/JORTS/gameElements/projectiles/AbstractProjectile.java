package JORTS.gameElements.projectiles;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.jsfml.graphics.CircleShape;
import org.jsfml.system.Vector2f;

import JORTS.behaviour.Attackable;
import JORTS.common.CommonFunctions;
import JORTS.core.Main;
import JORTS.gameElements.units.Entity;

public abstract class AbstractProjectile extends CircleShape implements Observer{
	public static ArrayList<AbstractProjectile> allProjectiles = new ArrayList<AbstractProjectile>();
	Vector2f startPoint;
	Vector2f targetPoint;
	Entity attacker;
	Attackable targetEntity;
	int dmg;
	int speed;
	int size=5;
	public AbstractProjectile (Entity attacker, Attackable targetEntity, Integer dmg,Integer speed)
	{
		super();
		//System.out.println("firing");
		setRadius(size);
		setOrigin(new Vector2f(getRadius(),getRadius()));
		this.attacker=attacker;
		this.startPoint=attacker.getPosition();
		this.targetPoint=targetEntity.getTargetPoint(startPoint);//targetEntity.getPosition();
		this.targetEntity=targetEntity;
		this.dmg=dmg;
		this.speed=speed;
		
		setPosition(startPoint);
		allProjectiles.add(this);
		Main.game.addObserver(this);
	}
	@Override
	public void update(Observable o, Object arg) 
	{
		if(CommonFunctions.getDist(getPosition(), targetPoint)<speed)
		{
			setPosition(targetPoint);
			terminate();
		}		
		else
		{
			this.move(CommonFunctions.limitVectorLength(Vector2f.sub(targetPoint,getPosition()), speed));
		}
	}
	public void terminate()
	{
		allProjectiles.remove(this);
		Main.game.deleteObserver(this);		
	}
}
