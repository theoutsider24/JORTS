package JORTS.gameElements.projectiles;

import java.util.TreeMap;

import JORTS.behaviour.Attackable;
import JORTS.gameElements.units.Entity;

public class ProjectileFactory <T extends AbstractProjectile>{
	private Class<T> c;
	public static TreeMap <String,ProjectileFactory> factories = new TreeMap <String,ProjectileFactory>(String.CASE_INSENSITIVE_ORDER);
	static{
		factories.put("Projectile", new ProjectileFactory<Projectile>(Projectile.class));
		factories.put("HomingProjectile", new ProjectileFactory<HomingProjectile>(HomingProjectile.class));
		factories.put("SplashProjectile", new ProjectileFactory<SplashProjectile>(SplashProjectile.class));
	}
    public ProjectileFactory(Class<T> c)
    {
        this.c = c;
    }

    public T buildProjectile(Entity attacker, Attackable targetEntity, int dmg,int speed) throws Exception
    {
    	return c.getConstructor(Entity.class,Attackable.class,Integer.class,Integer.class).newInstance(attacker,targetEntity,dmg,speed);	    	
    }
}
