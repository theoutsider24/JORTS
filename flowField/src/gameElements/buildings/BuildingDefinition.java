package gameElements.buildings;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class BuildingDefinition {
	String unitType="undefined";
	int maxHealth=1;
	int size=1;
	int constructionTime=0;
	ArrayList<String> production;
	public BuildingDefinition(JSONObject obj)
	{
		unitType=(String)obj.get("UnitType");
		maxHealth=(int)((long)obj.get("Health"));
		size=(int)((long)obj.get("Size"));		
		production = new ArrayList<String>();
		
		
		JSONArray productionList = (JSONArray) obj.get("Production");
		for(Object o:productionList)
		{
			production.add((String)((JSONObject)o).get("Name"));
			constructionTime = (int)((long)((JSONObject)o).get("Time"));	
		}
		
		BuildingFactory.addDefinition(this);
	}
}
