package JORTS.gameElements.buildings.resources;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.TreeMap;

import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Texture;

import JORTS.behaviour.orders.HarvestResourceOrder;
import JORTS.core.Player;
import JORTS.gameElements.buildings.Building;
import JORTS.uiComponents.HealthBar;

public class Resource extends Building
{
	private HarvestResourceOrder harvestOrder;
	static TreeMap<String,Texture> textures= new TreeMap<String,Texture>(String.CASE_INSENSITIVE_ORDER);
	public Resource(String type,int amount,int x,int y,Player p)
	{
		super(1);
		if(textures.containsKey(type))
			setTexture(textures.get(type));
		origin[0]=x;
		origin[1]=y;
		buildingType =type;
		id=id.replace("building", "resource_"+type);
		setMaxHealth(amount);
		p.addBuilding(this);
		placeFoundation();
		completeConstruction();
		healthBarEnabled=false;
		healthBar.setToGrayScale();
	}
	public HarvestResourceOrder getHarvestOrder()
	{
		if(harvestOrder==null)
		{
			harvestOrder=new HarvestResourceOrder(getSurroundOrder());
		}
		return harvestOrder;
	}
	public static void addTexture(String res,String file)
	{
		Texture texture;
		try 
		{
			texture=new Texture();
			texture.loadFromFile(Paths.get("imgs//"+file));
			textures.put(res, texture);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
