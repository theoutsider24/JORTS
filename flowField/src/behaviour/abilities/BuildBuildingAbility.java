package behaviour.abilities;

import org.jsfml.system.Vector2i;

import FYP.GameWindow;
import FYP.Main;
import FYP.Player;
import gameElements.buildings.Building;
import gameElements.buildings.BuildingFactory;
import gameElements.units.Entity;
import gameElements.units.UnitFactory;

public class BuildBuildingAbility extends Ability{
	String name;
	String building;
	Player p;
	public BuildBuildingAbility(String name,String building,Player p) {
		super(name);
		this.name=name;
		this.building=building;
		this.p=p;
	//	System.out.println(this.name);
	//	System.out.println(this.building);
	}
	@Override
	public void run() {
		GameWindow w= Main.getPlayerWindow(p);
			w.gui.cursor.attachBuilding(BuildingFactory.buildEntity(building,p));
		
	}
}
