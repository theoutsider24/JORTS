package buildings;

import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import FYP.Ability;
import FYP.Main;
import units.*;

public class Barracks extends Building {
	public Barracks()
	{
		super();
		buildingType="barracks";
		id=id.replace("building", "building_"+buildingType);
		
		abilities.add(new Ability("Spawn Infantry"){public void run(){
				Building b=Building.allBuildings.get(id);
				Vector2i spawnLoc = b.getSpawnLocation(); 
				b.player.addUnit(new Infantry(spawnLoc.x,spawnLoc.y));
			}}); 
		abilities.add(new Ability("Spawn Cavalry"){public void run(){
			Building b=Building.allBuildings.get(id);
			Vector2i spawnLoc = b.getSpawnLocation(); 
			b.player.addUnit(new Cavalry(spawnLoc.x,spawnLoc.y));
		}});
		abilities.add(new Ability("Spawn Siege Unit"){public void run(){
			Building b=Building.allBuildings.get(id);
			Vector2i spawnLoc = b.getSpawnLocation(); 
			b.player.addUnit(new SiegeUnit(spawnLoc.x,spawnLoc.y));
		}});
	}
}
