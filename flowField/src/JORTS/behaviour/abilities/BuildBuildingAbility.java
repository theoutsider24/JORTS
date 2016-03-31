package JORTS.behaviour.abilities;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import JORTS.common.ResourceAmount;
import JORTS.core.GameWindow;
import JORTS.core.Main;
import JORTS.core.Player;
import JORTS.gameElements.buildings.Building;
import JORTS.gameElements.buildings.BuildingFactory;
import JORTS.gameElements.units.UnitFactory;

public class BuildBuildingAbility extends Ability{
	public String building;
	Player p;
	ArrayList<ResourceAmount> requiredResources;
	public BuildBuildingAbility(String name,String building,Player p) 
	{
		super(name);
		this.building=building;
		this.p=p;
		
		JSONObject details=BuildingFactory.prototypes.get(building).costDetails;
		this.requiredResources=new ArrayList<ResourceAmount>();
		try{
		for(Object o:(JSONArray)details.get("resources"))
		{
			JSONObject j = (JSONObject)o;
			requiredResources.add(new ResourceAmount((String)j.get("resource"), (int)((long)j.get("amount"))));
		}}catch(NullPointerException ex){System.out.println("No resource Cost");}
	}
	@Override
	public void run() {
		if(canAfford())
		{
			GameWindow w= Main.getPlayerWindow(p);
			w.runCommand("placeBuilding "+building);
			for(ResourceAmount a:requiredResources)
			{
				p.collectResource(a.resource, -a.amount);
			}
		}
		else
		{
			Main.getPlayerWindow(p).setErrorMessage("Insufficient Resources");
		}
	}

	public boolean canAfford()
	{
		boolean canAfford=true;
		for(int i=0;i<requiredResources.size()&&canAfford;i++)
		{
			if(requiredResources.get(i).amount>p.resources.get(requiredResources.get(i).resource))
				canAfford=false;
		}
		return canAfford;
	}
	@Override
	public boolean canRun() {
		return canAfford();
	}
}
