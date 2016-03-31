package JORTS.gameElements.units;

import java.util.TreeMap;

import org.jsfml.system.Vector2f;

import JORTS.behaviour.abilities.BuildBuildingAbility;
import JORTS.core.Player;

public class UnitFactory {
	public static TreeMap<String,UnitDefinition> prototypes = new TreeMap<String,UnitDefinition>(String.CASE_INSENSITIVE_ORDER);
	public static Entity buildEntity(String type, Vector2f v,Player p)
	{
		return buildEntity(type,(int)v.x,(int)v.y,p);
	}
	public static Entity buildEntity(String type, int x,int y,Player p)
	{
		if(prototypes.containsKey(type))
		{
			UnitDefinition def = prototypes.get(type);
			
			Entity e= new Entity(x,y){};
			
			e.visionRange=def.visionRange;
			e.setUnitType(def.unitType);
			e.damage=def.damage;
			e.maxSpeed=(float) (def.maxSpeed);
			e.setMaxHealth(def.maxHealth);
			e.setRadius(def.radius);
			e.ranged=def.ranged;
			e.attackRange=def.attackRange;
			e.projectileType=def.projectileType;
			e.canConstruct=def.canConstruct;
			for(String s:def.buildings)
			{
				e.abilities.add(new BuildBuildingAbility("Build "+s,s,p));
			}
			
			if(def.texture!=null)
			{
				e.setTexture(def.texture);
			}
			
			return e;
		}
		return null;
	}
	public static void addDefinition(UnitDefinition def)
	{
		//System.out.println(def.unitType);
		if(!prototypes.containsKey(def.unitType))
			prototypes.put(def.unitType, def);
		else
			System.out.println("Attempted to redefine unit");
	}
}
