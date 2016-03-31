package JORTS.gameElements.units;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.jsfml.graphics.Texture;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class UnitDefinition {
	public String unitType="undefined";
	public double maxSpeed=1;
	public int maxHealth=1;
	public int radius=1;
	public int damage=1;
	public ArrayList<String> buildings;
	public Texture texture;
	public boolean canConstruct;
	String projectileType;
	boolean ranged;
	int attackRange;
	int visionRange;
	public JSONObject costDetails;
	
	public UnitDefinition(JSONObject obj)
	{
		unitType=(String)obj.get("UnitType");
		maxSpeed=(double)obj.getOrDefault("Speed",1L);	
		maxHealth=(int)((long)obj.getOrDefault("Health",100L));	
		radius=(int)((long)obj.getOrDefault("Radius",5L));	
		damage=(int)((long)obj.getOrDefault("Damage",10L));	
		visionRange=(int)((long)obj.getOrDefault("VisionRange",100L));	
		
		attackRange=(int)((long)obj.getOrDefault("Range",10L));	
		ranged = (boolean)(obj.getOrDefault("Ranged",false));
		canConstruct = (boolean)(obj.getOrDefault("canConstruct",false));
		if(ranged) projectileType = (String)obj.getOrDefault("ProjectileType","Projectile");
		
		buildings = new ArrayList<String>();
		
		JSONArray buildingList = (JSONArray) obj.get("Building");
		if(buildingList!=null)
			for(Object o:buildingList)
			{
				buildings.add((String)((JSONObject)o).get("Name"));
			}
		costDetails = (JSONObject)obj.get("Cost");
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
