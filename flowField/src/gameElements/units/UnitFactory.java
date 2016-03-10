package gameElements.units;

import java.util.HashMap;

import org.jsfml.graphics.IntRect;
import org.jsfml.system.Vector2f;

import FYP.Player;
import behaviour.abilities.BuildBuildingAbility;
import behaviour.abilities.CreateUnitAbility;

public class UnitFactory {
	static HashMap<String,UnitDefinition> prototypes = new HashMap<String,UnitDefinition>();
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
			
			e.setUnitType(def.unitType);
			
			e.maxSpeed=def.maxSpeed;
			e.setMaxHealth(def.maxHealth);
			e.setRadius(def.radius);
			
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
