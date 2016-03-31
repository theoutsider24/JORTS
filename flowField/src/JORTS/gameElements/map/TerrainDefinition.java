package JORTS.gameElements.map;

import java.io.IOException;
import java.nio.file.Paths;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Texture;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class TerrainDefinition {
	Texture texture;
	Color fillColor;
	boolean open;
	public String terrainType;
	public TerrainDefinition(JSONObject obj)
	{
		terrainType=(String)obj.get("Name");
		JSONArray rgb = (JSONArray)obj.get("Color");
		fillColor=new Color((int)((long)rgb.get(0)),(int)((long)rgb.get(1)),(int)((long)rgb.get(2)));
		open=(boolean)obj.get("Open");
		
		if(obj.get("Texture")!=null)
		{
			try 
			{
				texture=new Texture();
				texture.loadFromFile(Paths.get("imgs//"+((String)obj.get("Texture"))));
			}
			catch (IOException e) 
			{
				texture=null;
				e.printStackTrace();
			}
		}
		
		TerrainFactory.addDefinition(this);
	}
}
