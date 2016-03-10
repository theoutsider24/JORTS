package gameElements.buildings;

import java.util.HashMap;

import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import FYP.Player;
import behaviour.abilities.Ability;
import behaviour.abilities.CreateUnitAbility;
import gameElements.units.UnitFactory;

public class BuildingFactory {
	static HashMap<String,BuildingDefinition> prototypes = new HashMap<String,BuildingDefinition>();
	public static Building buildEntity(String type,Player p)
	{
		BuildingDefinition def = prototypes.get(type);
		
		Building b= new Building(){};
		b.buildingType =def.unitType;
		b.id=b.id.replace("building", "building_"+b.buildingType);
		
		for(String s:def.production)
		{
			b.abilities.add(new CreateUnitAbility("Spawn "+s,b.id,s,p,def.constructionTime));
		}
		
		return b;
	}
	public static void addDefinition(BuildingDefinition def)
	{
		if(!prototypes.containsKey(def.unitType))
			prototypes.put(def.unitType, def);
		else
			System.out.println("Attempted to redefine unit");
	}
}
