package uiComponents.grids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jsfml.system.Vector2f;

import FYP.Ability;
import FYP.Main;
import uiComponents.buttons.StandardButton;
import uiComponents.buttons.uiButton;
import units.Entity;

public class ActionButtonGrid extends StandardButtonGrid{
	String currentUnitType="";
	public ActionButtonGrid(int x, int y, Vector2f pos) {
		super(3, 3, pos);
	}
	public void setAbilities(ArrayList<Ability> abilities)
	{
		int i=0;
		for(i=0;i<abilities.size()&&i<9;i++)
		{
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
		for(Entity e:Main.activePlayer.getSelectedUnits())
		{
			if(counter.containsKey(e.unitType))
			{
				counter.put(e.unitType,counter.get(e.unitType)+1);
			}
			else
				counter.put(e.unitType,0);
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
	    if(!type.equals(currentUnitType))
	    {
	    	removeAll();
	    	currentUnitType=type;
		    outer:for(Entity e:Main.activePlayer.getSelectedUnits())
			{
		    	if(e.unitType.equals(type))
		    	{
		    		setAbilities(e.abilities);
		    		break outer;
		    	}
			}
	    }	    
	}
	
}
