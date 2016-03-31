package JORTS.gameElements.projectiles;

import JORTS.behaviour.Attackable;
import JORTS.gameElements.units.Entity;

public class Projectile extends AbstractProjectile
{
	public Projectile(Entity attacker, Attackable targetEntity, Integer dmg, Integer speed) {
		super(attacker, targetEntity, dmg, speed);
	}
	@Override
	public void terminate()
	{
		super.terminate();
		if(targetEntity.getDistance(this)<0/*&&targetEntity.getOwner()!=attacker.player*/)
			targetEntity.updateHealth(-dmg);
	}
	
}
