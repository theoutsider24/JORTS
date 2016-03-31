package JORTS.uiComponents;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.jsfml.system.Vector2f;

import JORTS.gameElements.map.TerrainDefinition;
import JORTS.gameElements.map.TerrainFactory;
import JORTS.uiComponents.buttons.StandardButton;
import JORTS.uiComponents.grids.StandardButtonGrid;

public class MapEditorGui extends StandardButtonGrid{
	public String terrainType="";
	public MapEditorGui(GUI gui)
	{
		super(1,TerrainFactory.prototypes.size(),new Vector2f(20,300),gui);
		Iterator it= TerrainFactory.prototypes.entrySet().iterator();
		int i=0;
		while(it.hasNext())
		{
			Entry<String,TerrainDefinition> pair = (Entry<String,TerrainDefinition>)it.next();
			TerrainDefinition t=pair.getValue();
			addButton(new StandardButton(t.terrainType){
				@Override
				public void click()
				{
					terrainType=t.terrainType;
				}
			}, 0, i++);
		}
	}
}
