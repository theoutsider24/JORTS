package JORTS.communication;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import JORTS.behaviour.abilities.Ability;
import JORTS.behaviour.abilities.BuildBuildingAbility;
import JORTS.behaviour.abilities.CreateUnitAbility;
import JORTS.behaviour.orders.Order;
import JORTS.common.ResourceAmount;
import JORTS.core.Main;
import JORTS.core.Player;
import JORTS.gameElements.buildings.Building;
import JORTS.gameElements.buildings.BuildingDefinition;
import JORTS.gameElements.buildings.BuildingFactory;
import JORTS.gameElements.buildings.resources.Resource;
import JORTS.gameElements.units.Entity;
import JORTS.gameElements.units.UnitDefinition;
import JORTS.gameElements.units.UnitFactory;
import JORTS.uiComponents.textFields.ResourceList;

public class QueryProcessor {
	Server server;
	public QueryProcessor(Server s)
	{
		server=s;
	}
	public void processQuery(String s)
	{
		String id=s.split(":")[0].trim();
		s=s.toLowerCase();
		String query=s.replace(id+":","");
		
		String response="";
		System.out.println(query);
		switch(query)
		{
		case "what can i build?":response=whatCanIBuildResponse();break;
		case "what units are in the game?":response=getUnitList();break;
		case "what buildings are in the game?":response=getBuildingList();break;
		case "what resources are in the game?":response=getResourceList();break;
		case "who are the players?":response=getPlayerList();break;
		case "get random idle villager":response=getRandomIdleVillager();break;
		default :response="other"; break;
		}
		if(response.equals("other"))
		{
			if(query.contains("what produces"))response=whatProducesUnit(query);
			else if(query.contains("what type of building"))response=getBuildingType(query);
			else if(query.contains("what type of unit"))response=getUnitType(query);
			else if(query.contains("what resources are required"))response=getRequiredResources(query);
			else if(query.contains("how much"))response=getCurrentResources(query);
			else if(query.contains("produce unit"))response=produceUnit(query);
			else if(query.contains("collect"))collectResource(query);
		}
		response=id+":"+response;
		server.sendMessage(server.player, response);
	}
	private void collectResource(String query) {
		String unit=query.split("\"")[1];
		String resource=query.split("\"")[3];
		
		Building b=server.eventManager.visibleBuildings.get(resource);
		Resource r=(Resource)b;
		r.getHarvestOrder().issue(server.eventManager.visibleUnits.get(unit));
	}
	private String getRandomIdleVillager() {
		for(Entity e:server.player.getUnits())
		{
			if(e.canConstruct&&e.currentOrder==Order.IdleOrder)//TODO change to canCollect
			{
				for(String s:server.eventManager.visibleUnits.keySet())
				{
					if(server.eventManager.visibleUnits.get(s)==e)
						return s;
				}
			}
		}
		return "";
	}
	private String produceUnit(String query) {
		String type=query.split("\"")[1];
		String id=query.split("\"")[3];
		Building b=server.eventManager.visibleBuildings.get(id);
		for(Ability a:b.abilities)
		{
			if(a.name.toLowerCase().contains("spawn"))
			{
				CreateUnitAbility c = (CreateUnitAbility)a;
				if(c.unitToBuild.toLowerCase().equals(type.toLowerCase()))
					c.run();
			}
		}
		return type;
	}
	private String getCurrentResources(String query) {
		String amount="";
		String type=query.split("\"")[1];
		System.out.println(type);
		amount+=server.player.resources.get(type);
		System.out.println(amount);
		return amount;
	}
	private String getRequiredResources(String query) {
		String resources="";
		String type=query.split("\"")[1];
		if(UnitFactory.prototypes.containsKey(type))
		{
			UnitDefinition def= UnitFactory.prototypes.get(type);
			JSONObject details=def.costDetails;
			try{
			for(Object o:(JSONArray)details.get("resources"))
			{
				JSONObject j = (JSONObject)o;
				resources+=","+(((String)j.get("resource")) +":"+(int)((long)j.get("amount")));
			}}catch(NullPointerException ex){}
		}
		else if(BuildingFactory.prototypes.containsKey(type))
		{
			BuildingDefinition def= BuildingFactory.prototypes.get(type);
			JSONObject details=def.costDetails;
			try{
			for(Object o:(JSONArray)details.get("resources"))
			{
				JSONObject j = (JSONObject)o;
				resources+=","+(((String)j.get("resource")) +":"+(int)((long)j.get("amount")));
			}}catch(NullPointerException ex){}
		}
		if(!resources.equals(""))
		{
			resources=resources.replaceFirst(",", "");
		}
		return resources;
	}
	public String whatProducesUnit(String query)
	{
		String buildings="";
		String unitType=query.split("\"")[1];
		for(Entry<String, BuildingDefinition> def:BuildingFactory.prototypes.entrySet())
		{
			if(def.getValue().production.contains(unitType))
				buildings+=","+def.getKey();
		}
		if(!buildings.equals(""))
		{
			buildings=buildings.replaceFirst(",", "");
		}
		return buildings;
	}
	public String whatCanIBuildResponse()
	{
		String response="";
		for(Entity e:server.player.getUnits())
		{
			if(e.canConstruct)
			{
				for(Ability a:e.abilities)
				{
					if(a.name.toLowerCase().contains("build"))
					{
						String b=((BuildBuildingAbility)a).building;
						System.out.println(b);
						if(!response.contains(b))
							response+=","+b;
					}
				}
			}
		}
		if(!response.equals(""))
		{
			response=response.replaceFirst(",", "");
		}
		return response;
	}
	public String getUnitList()
	{
		String response="";
		for(String s:UnitFactory.prototypes.keySet())
		{
			response+=","+s;
		}
		if(!response.equals(""))
		{
			response=response.replaceFirst(",", "");
		}
		return response;
	}
	public String getResourceList()
	{
		String response="";
		for(String s:ResourceList.resources)
		{
			response+=","+s;
		}
		if(!response.equals(""))
		{
			response=response.replaceFirst(",", "");
		}
		return response;
	}
	public String getBuildingList()
	{
		String response="";
		for(String s:BuildingFactory.prototypes.keySet())
		{
			response+=","+s;
		}
		if(!response.equals(""))
		{
			response=response.replaceFirst(",", "");
		}
		return response;
	}
	public String getPlayerList()
	{
		String response="";
		for(Player p:Main.players)
		{
			response+=","+p.name;
		}
		if(!response.equals(""))
		{
			response=response.replaceFirst(",", "");
		}
		return response;
	}
	public String getBuildingType(String query)
	{
		String type="";
		String id=query.split("\"")[1];
		type=server.eventManager.visibleBuildings.get(id).buildingType;
		return type;
	}
	public String getUnitType(String query)
	{
		String type="";
		String id=query.split("\"")[1];
		type=server.eventManager.visibleUnits.get(id).unitType;
		return type;
	}
}
