package JORTS.uiComponents.grids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jsfml.system.Vector2f;

import JORTS.behaviour.abilities.Ability;
import JORTS.gameElements.buildings.Building;
import JORTS.gameElements.units.Entity;
import JORTS.uiComponents.GUI;
import JORTS.uiComponents.buttons.StandardButton;
import JORTS.uiComponents.buttons.uiButton;

public class ActionButtonGrid extends StandardButtonGrid{
	String currentType="";
	public ActionButtonGrid(int x, int y, Vector2f pos,GUI gui) {
		super(3, 3, pos,gui);
	}
	public void setAbilities(ArrayList<Ability> abilities)
	{
		int i=0;
		for(i=0;i<abilities.size()&&i<9;i++)
		{
			if(!abilities.get(i).enabled)
				continue;
			if(!uiButton.allButtons.containsKey(abilities.get(i).getName()))
			{
				uiButton b = new StandardButton(abilities.get(i).getName()){public void click(){				
					activity.run();
				}};
				b.activity=abilities.get(i);
				addButton(b,i%3,i/3);
			}
		}
	}
	public void update()
	{
		HashMap<String,Integer> counter = new HashMap<String,Integer>();
		if(gui.window.activePlayer.getSelectedUnits().size()>0)
		{
			for(Entity e:gui.window.activePlayer.getSelectedUnits())
			{
				if(counter.containsKey(e.unitType))
				{
					counter.put(e.unitType,counter.get(e.unitType)+1);
				}
				else
					counter.put(e.unitType,1);
			}
			int most=0;
			String type="";
		    Iterator it = counter.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        if((Integer)pair.getValue()>most)
		        {
		        	most=(Integer)pair.getValue();
		        	type=(String)pair.getKey();
		        }
		        it.remove();
		    }
		    if(!type.equals(currentType))
		    {
		    	removeAll();
		    	currentType=type;
			    outer:for(Entity e:gui.window.activePlayer.getSelectedUnits())
				{
			    	if(e.unitType.equals(type))
			    	{
			    		setAbilities(e.abilities);
			    		break outer;
			    	}
				}
		    }
		   
		}
		else if(gui.window.activePlayer.getSelectedBuildings().size()>0)
		{
			for(Building b:gui.window.activePlayer.getSelectedBuildings())
			{
				if(counter.containsKey(b.buildingType))
				{
					counter.put(b.buildingType,counter.get(b.buildingType)+1);
				}
				else
					counter.put(b.buildingType,1);
			}
			int most=0;
			String type="";
		    Iterator it = counter.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        if((Integer)pair.getValue()>most)
		        {
		        	most=(Integer)pair.getValue();
		        	type=(String)pair.getKey();
		        }
		        it.remove();
		    }
		    if(!type.equals(currentType))
		    {
		    	removeAll();
		    	currentType=type;
			    outer:for(Building b:gui.window.activePlayer.getSelectedBuildings())
				{
			    	if(b.buildingType.equals(type))
			    	{
			    		setAbilities(b.abilities);
			    		break outer;
			    	}
				}
		    }
		}
		else
		{
			removeAll();
			currentType="";
		}
	}
	
}
