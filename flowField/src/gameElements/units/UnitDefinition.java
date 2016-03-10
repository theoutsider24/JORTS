package gameElements.units;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.jsfml.graphics.Image;
import org.jsfml.graphics.Texture;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class UnitDefinition {
	String unitType="undefined";
	int maxSpeed=1;
	int maxHealth=1;
	int radius=1;
	ArrayList<String> buildings;
	Image icon;
	Texture texture;
	public UnitDefinition(JSONObject obj)
	{
		unitType=(String)obj.get("UnitType");
		maxSpeed=(int)((long)obj.get("Speed"));
		maxHealth=(int)((long)obj.get("Health"));
		radius=(int)((long)obj.get("Radius"));		
		buildings = new ArrayList<String>();
		
		JSONArray buildingList = (JSONArray) obj.get("Building");
		if(buildingList!=null)
			for(Object o:buildingList)
			{
				buildings.add((String)((JSONObject)o).get("Name"));
			}
		
		icon=new Image();
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
		
		UnitFactory.addDefinition(this);
	}
}
