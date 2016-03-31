package JORTS.gameElements.projectiles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.jsfml.system.Vector2f;

import JORTS.behaviour.Attackable;
import JORTS.gameElements.Animation;
import JORTS.gameElements.units.Entity;

public class SplashProjectile extends AbstractProjectile{
	int splashRange=50;
	
	public SplashProjectile(Entity attacker, Attackable targetEntity, Integer dmg,Integer speed) {
		super(attacker, targetEntity, dmg, speed);
	}
	public void setSplashRange(int range)
	{
		splashRange=range;
	}
	@Override
	public void terminate()
	{
		super.terminate();
		setRadius(splashRange);
		setOrigin(new Vector2f(getRadius(),getRadius()));
		ArrayList<Attackable> thingsToDamage = new ArrayList<Attackable>();
		Iterator it = Entity.allEntities.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry pair = (Map.Entry)it.next();
			Attackable a=(Attackable) pair.getValue();
			
			
			if(a.getDistance(this)<0)
				thingsToDamage.add(a);
			//it.remove();
		}
		for(Attackable a:thingsToDamage)
			a.updateHealth(-attacker.damage);

		
		Animation.createExplosionAnimation(getPosition(), new Vector2f(splashRange*2,splashRange*2));
	}
}
