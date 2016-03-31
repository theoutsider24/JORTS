package JORTS.gameElements.buildings;

import java.util.TreeMap;

import org.json.simple.JSONObject;

import JORTS.behaviour.abilities.CreateUnitAbility;
import JORTS.core.Main;
import JORTS.core.Player;
import JORTS.gameElements.map.MapCell;

public class BuildingFactory {
	public static TreeMap<String,BuildingDefinition> prototypes = new TreeMap<String,BuildingDefinition>(String.CASE_INSENSITIVE_ORDER);
	public static Building buildBuilding(String type,Player p)
	{
		BuildingDefinition def = prototypes.get(type);
		
		Building b= new Building(def.size){};
		b.buildingType =def.unitType;
		b.id=b.id.replace("building", "building_"+b.buildingType);
		b.setMaxHealth(def.maxHealth);
		b.visionRange=def.visionRange;
		for(String s:def.production)
		{
			try{b.abilities.add(new CreateUnitAbility(b.id,s,p));}
			catch(Exception e){System.out.println("Ability add failed");}
		}
		if(def.texture!=null)
		{
			b.setTexture(def.texture);
		}
		b.resourceRepository=def.resourceRepository;
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
			b.placeFoundation();
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
