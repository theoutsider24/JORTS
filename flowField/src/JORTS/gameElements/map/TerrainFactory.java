package JORTS.gameElements.map;

import java.util.TreeMap;

public class TerrainFactory {
	public static TreeMap<String,TerrainDefinition> prototypes = new TreeMap<String,TerrainDefinition>(String.CASE_INSENSITIVE_ORDER);
	public static String defaultTerrain;
	public static void addDefinition(TerrainDefinition def)
	{
		if(!prototypes.containsKey(def.terrainType))
			prototypes.put(def.terrainType, def);
		else
			System.out.println("Attempted to redefine unit");
	}
	public static MapCell buildCell(int x,int y, String terrainType)
	{
		MapCell c= new MapCell(x, y);
		return buildCell(c,terrainType);
	}
	public static MapCell buildCell(MapCell c, String terrainType)
	{
		if(prototypes.containsKey(terrainType))
		{
			TerrainDefinition def = prototypes.get(terrainType);
			//c.setFillColor(def.fillColor);
			c.color=def.fillColor;
			c.setFillColor(def.fillColor);
			c.setTexture(null);
			if(def.texture!=null)
			{
				c.setTexture(def.texture);
			}
			if(!def.open)
				c.close();
			else
				c.open();
			c.terrain=def;
		}
		else
		{
			c=buildCell(c, defaultTerrain);
		}
		return c;
	}
}
