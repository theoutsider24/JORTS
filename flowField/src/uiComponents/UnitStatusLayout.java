package uiComponents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import FYP.GameWindow;
import gameElements.buildings.Building;
import gameElements.units.Entity;
import gameElements.units.UnitDefinition;
import gameElements.units.UnitFactory;
import uiComponents.buttons.StandardButton;
import uiComponents.buttons.uiButton;
import uiComponents.grids.ButtonGrid;

public class UnitStatusLayout implements Drawable{
	RectangleShape background;
	GUI gui;
	public ButtonGrid slots;
	public ArrayList<Entity> units;
	public UnitDetailsPanel detailsPanel;
	public UnitStatusLayout(GUI gui) 
	{
		this.gui=gui;
		
		units= new ArrayList<Entity>();
		
		background=new RectangleShape();
		background.setPosition(700,780);
		background.setSize(new Vector2f(500,200));
		background.setFillColor(new Color(150,150,150));
		background.setOutlineColor(new Color(30,30,30));
		background.setOutlineThickness(3);
		
		slots=new ButtonGrid(10, 3, new Vector2f(760,810), new Vector2i(40,40),gui){};		
	}
	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) 
	{
		arg0.draw(background);
		arg0.draw(slots);
		arg0.draw(detailsPanel);
	}
	public void update(ArrayList<Entity> units)
	{
		try{detailsPanel.update();}catch(NullPointerException ex){}
		if(selectedUnitsChanged())
		{

			System.out.println("changed");
			this.units.clear();
			slots.removeAll();
			for(Entity e:units)
			{
				this.units.add(e);
			}

			HashMap<String, Integer> types = new HashMap<String, Integer>();				
			for(Entity e:units)
			{
				if(!types.containsKey(e.unitType))
					types.put(e.unitType,1);
				else
					types.put(e.unitType,types.get(e.unitType)+1);
			}
			
			int most=0;
			String type="";
		    Iterator it = types.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        if((Integer)pair.getValue()>most)
		        {
		        	most=(Integer)pair.getValue();
		        	type=(String)pair.getKey();
		        }
		    }
		    for(Entity e:units)
		    {
		    	if(e.unitType.equals(type))
		    	{
		    		setDetailsPanel(e);
		    		break;
		    	}
		    }
		    System.out.println(type);
			
			if(units.size()<=30)
			{
				addIndividualButtons();
			}
			else
			{
				addGroupedButtons(types);
			}
		}
	}
	public void setDetailsPanel(Entity e)
	{
		detailsPanel=new UnitDetailsPanel(e);
		detailsPanel.setPosition(Vector2f.add(background.getPosition(),new Vector2f(-100,0)));
	}
	public void addGroupedButtons(HashMap<String, Integer> types)
	{		
		int x=0,y=0;
		Iterator it = types.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
        	int amount=(Integer)pair.getValue();
        	String type=(String)pair.getKey();
	        
        	if(x>9){x=0;y++;}
        	uiButton b =new StandardButton(amount+""){
    			@Override public void click(){
	    				ArrayList<Entity> es = new ArrayList<Entity>();
	    				for(Entity e:units)
	    				{
	    					if(e.unitType.equals(type))
	    						es.add(e);
	    				}
	    				gui.window.activePlayer.selectUnits(es);
    				}};	
			//b.text.setColor(Color.BLACK);
        	slots.addButton(b,x,y);
        	try{
				b.setTexture(((UnitDefinition)UnitFactory.prototypes.get(type)).texture);
				b.setFillColor(Color.WHITE);
			}
			catch(Exception e){};
        	x++;
	    }
	}
	public void addIndividualButtons()
	{
		int x=0,y=0;
		for(Entity e:units)
		{
			if(x>9){x=0;y++;}
			addButton(e, x, y);			
			x++;
		}
	}
	public void addButton(Entity unit,int x,int y)
	{
		uiButton b= new StandardButton(""){
			@Override public void click(){
				gui.window.activePlayer.selectUnit(unit.id);
				}};
				try{
					b.setTexture(((UnitDefinition)UnitFactory.prototypes.get(unit.unitType)).texture);
					b.setFillColor(Color.WHITE);
				}
				catch(Exception e){};
		slots.addButton(b, x, y);		
	}
	public boolean selectedUnitsChanged()
	{
		if(units.size()!=gui.window.activePlayer.getSelectedUnits().size())
			return true;
		for(Entity e:units)
		{
			if(!gui.window.activePlayer.getSelectedUnits().contains(e))
				return true;
		}
		return false;
	}
}
