package behaviour.abilities;

import java.util.ArrayList;
import java.util.Observable;

import org.jsfml.system.Vector2i;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import FYP.Player;
import behaviour.timedBehaviours.TimedBehaviour;
import common.ResourceAmount;
import gameElements.buildings.Building;
import gameElements.units.Entity;
import gameElements.units.UnitFactory;

public class CreateUnitAbility extends Ability{
	String builder;
	String unitToBuild;
	Player p;
	ArrayList<ResourceAmount> requiredResources;
	int constructionTime;
	public CreateUnitAbility(String builder, JSONObject details,Player p) {
		super("Spawn "+(String)details.get("unitType"));

		
		this.requiredResources=new ArrayList<ResourceAmount>();
		try{
		for(Object o:(JSONArray)details.get("resources"))
		{
			JSONObject j = (JSONObject)o;
			requiredResources.add(new ResourceAmount((String)j.get("resource"), (int)((long)j.get("amount"))));
		}}catch(NullPointerException ex){System.out.println("No resource Cost");}
		this.constructionTime=(int)((long)details.get("time"));
		this.builder=builder;	
		this.unitToBuild=(String)details.get("unitType");
		this.name="Spawn "+unitToBuild;
		this.p=p;
	//	System.out.println(this.ownerId);
		//System.out.println(this.building);
	}
	@Override
	public void run() {
		Building b=Building.allBuildings.get(builder);
		b.startProduction(unitToBuild,constructionTime);
	}
	
}
