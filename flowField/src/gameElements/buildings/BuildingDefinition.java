package gameElements.buildings;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Image;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.TextureCreationException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class BuildingDefinition {
	String unitType="undefined";
	int maxHealth=1;
	int size=1;
	int constructionTime=0;
	ArrayList<JSONObject> production;
	Texture texture;
	public BuildingDefinition(JSONObject obj)
	{
		unitType=(String)obj.get("UnitType");
		maxHealth=(int)((long)obj.get("Health"));
		size=(int)((long)obj.get("Size"));		
		production = new ArrayList<JSONObject>();
		
		
		JSONArray productionList = (JSONArray) obj.get("Production");
		if(productionList!=null)for(Object o:productionList)
		{
			production.add((JSONObject)o);
		}
		if(obj.get("Icon")!=null)
		{
			try 
			{
			/*	Image img = new Image();
				img.loadFromFile(Paths.get("imgs//"+((String)obj.get("Icon"))));
				img.createMaskFromColor(Color.MAGENTA);
				*/
				texture=new Texture();
				texture.loadFromFile(Paths.get("imgs//"+((String)obj.get("Icon"))));
				//texture.loadFromImage(img);
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
