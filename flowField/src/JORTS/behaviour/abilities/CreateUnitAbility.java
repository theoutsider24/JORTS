package JORTS.behaviour.abilities;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import JORTS.common.ResourceAmount;
import JORTS.core.Main;
import JORTS.core.Player;
import JORTS.gameElements.buildings.Building;
import JORTS.gameElements.units.UnitFactory;

public class CreateUnitAbility extends Ability{
	String builder;
	public String unitToBuild;
	Player p;
	ArrayList<ResourceAmount> requiredResources;
	int constructionTime;
	public CreateUnitAbility(String builder, String unitType,Player p) {
		super("Spawn "+unitType);
		JSONObject details=UnitFactory.prototypes.get(unitType).costDetails;
		
		this.requiredResources=new ArrayList<ResourceAmount>();
		try{
		for(Object o:(JSONArray)details.get("resources"))
		{
			JSONObject j = (JSONObject)o;
			requiredResources.add(new ResourceAmount((String)j.get("resource"), (int)((long)j.get("amount"))));
		}}catch(NullPointerException ex){System.out.println("No resource Cost");}
		this.constructionTime=(int)((long)details.get("time"));
		this.builder=builder;	
		this.unitToBuild=unitType;
		this.name="Spawn "+unitToBuild;
		this.p=p;
		

	}
	@Override
	public void run() {
		if(canAfford())
		{
			Building b=Building.allBuildings.get(builder);
	
			for(ResourceAmount a:requiredResources)
			{
				b.player.collectResource(a.resource, -a.amount);
			}
			b.startProduction(unitToBuild,constructionTime,requiredResources);
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
