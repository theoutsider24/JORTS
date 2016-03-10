package behaviour.abilities;

import java.util.Observable;

import org.jsfml.system.Vector2i;

import FYP.Player;
import behaviour.timedBehaviours.TimedBehaviour;
import gameElements.buildings.Building;
import gameElements.units.Entity;
import gameElements.units.UnitFactory;

public class CreateUnitAbility extends Ability{
	String name;
	String ownerId;
	String building;
	Player p;
	int constructionTime;
	public CreateUnitAbility(String name,String ownerId,String building,Player p,int t) {
		super(name);
		this.name=name;
		this.ownerId=ownerId;	
		this.building=building;
		this.p=p;
		this.constructionTime=t;
	//	System.out.println(this.name);
	//	System.out.println(this.ownerId);
		//System.out.println(this.building);
	}
	@Override
	public void run() {
		Building b=Building.allBuildings.get(ownerId);
		b.startProduction(building,2000);
		/*new TimedBehaviour(constructionTime)
		{
			@Override
			public void run(){
				Building b=Building.allBuildings.get(ownerId);
				Vector2i spawnLoc = b.getSpawnLocation(); 
				Entity e=UnitFactory.buildEntity(building,spawnLoc.x,spawnLoc.y,p);
				if(e!=null)
					b.player.addUnit(e);
			}
		};*/
		
	}
	
}
