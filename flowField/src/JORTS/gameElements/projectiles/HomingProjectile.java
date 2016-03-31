package JORTS.gameElements.projectiles;

import java.util.Observable;

import org.jsfml.system.Vector2f;

import JORTS.behaviour.Attackable;
import JORTS.common.CommonFunctions;
import JORTS.gameElements.units.Entity;

public class HomingProjectile extends Projectile{

	public HomingProjectile(Entity attacker, Attackable targetEntity, Integer dmg, Integer speed) {
		super(attacker, targetEntity, dmg, speed);
	}
	@Override
	public void update(Observable o, Object arg) 
	{
		if(CommonFunctions.getDist(getPosition(), targetEntity.getPosition())<speed)
		{
			setPosition(targetEntity.getPosition());
			terminate();
		}		
		else
		{
			this.move(CommonFunctions.limitVectorLength(Vector2f.sub(targetEntity.getPosition(),getPosition()), speed));
		}
	}
}
