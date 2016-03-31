package JORTS.gameElements.buildings;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.jsfml.graphics.Texture;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class BuildingDefinition {
	public String unitType="undefined";
	int maxHealth=1;
	int size=1;
	int constructionTime=0;
	int visionRange;
	public ArrayList<String> production;
	public Texture texture;
	public ArrayList<String> resourceRepository=new ArrayList<String>();
	public JSONObject costDetails;
	public BuildingDefinition(JSONObject obj)
	{
		unitType=(String)obj.get("UnitType");
		maxHealth=(int)((long)obj.get("Health"));
		size=(int)((long)obj.get("Size"));		
		production = new ArrayList<String>();
		visionRange=(int)((long)obj.getOrDefault("VisionRange",200L));	
		
		JSONArray repository = (JSONArray) obj.get("repository");
		if(repository!=null)
			for(Object o:repository)
			{
				resourceRepository.add((String)o);
			}
		costDetails = (JSONObject)obj.get("Cost");
		JSONArray productionList = (JSONArray) obj.get("Production");
		if(productionList!=null)
			for(Object o:productionList)
			{
				production.add((String)o);
			}
		if(obj.get("Icon")!=null)
		{
			try 
			{
				texture=new Texture();
				texture.loadFromFile(Paths.get("imgs//"+((String)obj.get("Icon"))));
			}
			catch (IOException e) 
			{
				texture=null;
				e.printStackTrace();
			}
			
		}
		BuildingFactory.addDefinition(this);
	}
}
