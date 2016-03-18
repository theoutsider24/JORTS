package gameElements.buildings;

import java.util.HashMap;
import java.util.TreeMap;

import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.json.simple.JSONObject;

import FYP.Main;
import FYP.Player;
import behaviour.abilities.Ability;
import behaviour.abilities.CreateUnitAbility;
import gameElements.map.MapCell;
import gameElements.units.UnitFactory;

public class BuildingFactory {
	static TreeMap<String,BuildingDefinition> prototypes = new TreeMap<String,BuildingDefinition>(String.CASE_INSENSITIVE_ORDER);
	public static Building buildBuilding(String type,Player p)
	{
		BuildingDefinition def = prototypes.get(type);
		
		Building b= new Building(def.size){};
		b.buildingType =def.unitType;
		b.id=b.id.replace("building", "building_"+b.buildingType);
		b.setMaxHealth(def.maxHealth);
		for(JSONObject s:def.production)
		{
			try{b.abilities.add(new CreateUnitAbility(b.id,s,p));}
			catch(Exception e){System.out.println("Ability add failed");}
		}
		if(def.texture!=null)
		{
			b.setTexture(def.texture);
		}
		return b;
	}
	public static Building buildBuilding(String type,Player p,int x,int y)
	{
		Building b = buildBuilding(type, p);
		b.origin[0]=x;
		b.origin[1]=y;
		MapCell c=Main.worldMap.getCell(x, y);
		for(int[] offset:b.offsets)
			if(!Main.worldMap.isCellTraversable(c.x+offset[0], c.y+offset[1]))
				b.valid=false;
		if(b.valid)
		{
			p.addBuilding(b);
			b.place();
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
