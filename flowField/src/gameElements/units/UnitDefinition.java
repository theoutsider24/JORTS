package gameElements.units;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.jsfml.graphics.Image;
import org.jsfml.graphics.Texture;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class UnitDefinition {
	public String unitType="undefined";
	public int maxSpeed=1;
	public int maxHealth=1;
	public int radius=1;
	public int damage=1;
	public ArrayList<String> buildings;
	public Texture texture;
	public UnitDefinition(JSONObject obj)
	{
		unitType=(String)obj.get("UnitType");
		maxSpeed=(int)((long)obj.get("Speed"));
		maxHealth=(int)((long)obj.get("Health"));
		radius=(int)((long)obj.get("Radius"));	
		damage=(int)((long)obj.get("Damage"));	
		buildings = new ArrayList<String>();
		
		JSONArray buildingList = (JSONArray) obj.get("Building");
		if(buildingList!=null)
			for(Object o:buildingList)
			{
				buildings.add((String)((JSONObject)o).get("Name"));
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
		
		UnitFactory.addDefinition(this);
	}
}
