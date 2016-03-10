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
				b.player.addUnit(UnitFactory.buildEntity("infantry",spawnLoc.x,spawnLoc.y));
			}}); 
		abilities.add(new Ability("Spawn Cavalry"){public void run(){
			Building b=Building.allBuildings.get(id);
			Vector2i spawnLoc = b.getSpawnLocation(); 
			b.player.addUnit(UnitFactory.buildEntity("cavalry",spawnLoc.x,spawnLoc.y));
		}});
		abilities.add(new Ability("Spawn Siege Unit"){public void run(){
			Building b=Building.allBuildings.get(id);
			Vector2i spawnLoc = b.getSpawnLocation(); 
			b.player.addUnit(UnitFactory.buildEntity("siege_unit",spawnLoc.x,spawnLoc.y));
		}});
	}
}
