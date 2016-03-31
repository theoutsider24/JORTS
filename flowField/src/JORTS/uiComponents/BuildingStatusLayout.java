package JORTS.uiComponents;

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

import JORTS.behaviour.timedBehaviours.TimedBehaviour;
import JORTS.gameElements.buildings.Building;
import JORTS.gameElements.buildings.BuildingFactory;
import JORTS.gameElements.units.Entity;
import JORTS.gameElements.units.UnitFactory;
import JORTS.uiComponents.buttons.StandardButton;
import JORTS.uiComponents.buttons.uiButton;
import JORTS.uiComponents.grids.ButtonGrid;

public class BuildingStatusLayout implements Drawable{
	GUI gui;
	public static ProgressBar progressBar;
	RectangleShape background;
	public ButtonGrid slots;
	public uiButton currentSlot;
	public BuildingDetailsPanel detailsPanel;
	ArrayList<TimedBehaviour> currentProduction;
	Building building;
	public ArrayList<Building> buildings;
	
	public ButtonGrid selectionSlots;
	
	public BuildingStatusLayout(GUI g){
		currentProduction=new ArrayList<TimedBehaviour>();
		currentProduction.add(null);
		currentProduction.add(null);
		currentProduction.add(null);
		currentProduction.add(null);
		currentProduction.add(null);
		currentProduction.add(null);
		
		buildings= new ArrayList<Building>();
		
		gui=g;
		progressBar=new ProgressBar();
		progressBar.setSize(250, 30);
		progressBar.setPosition(925,805);
		
		background=new RectangleShape();
		background.setPosition(700,780);
		background.setSize(new Vector2f(500,200));
		background.setFillColor(new Color(150,150,150));
		background.setOutlineColor(new Color(30,30,30));
		background.setOutlineThickness(3);
		
		slots=new ButtonGrid(5, 1, new Vector2f(940,850), new Vector2i(40,40),gui){};		
		slots.addButton(new StandardButton("1"), 0, 0);
		slots.addButton(new StandardButton("2"), 1, 0);
		slots.addButton(new StandardButton("3"), 2, 0);
		slots.addButton(new StandardButton("4"), 3, 0);
		slots.addButton(new StandardButton("5"), 4, 0);
		
		currentSlot=new uiButton("",100,100){};		
		currentSlot.setPosition(800,800);
		currentSlot.disable();
		
		selectionSlots=new ButtonGrid(10, 3, new Vector2f(760,810), new Vector2i(40,40),gui){};	
	}
	public void update(ArrayList<Building> buildings)
	{
		if(selectedBuildingsChanged()||buildings.size()==1)
		{
			if(selectedBuildingsChanged())
			{
				this.buildings.clear();
				selectionSlots.removeAll();
				for(Building b:buildings)
				{
					this.buildings.add(b);
				}
			}
			if(buildings.size()==1)
				update(this.buildings.get(0));
			else 
			{
				HashMap<String, Integer> types = new HashMap<String, Integer>();				
				for(Building b:buildings)
				{
					if(!types.containsKey(b.buildingType))
						types.put(b.buildingType,1);
					else
						types.put(b.buildingType,types.get(b.buildingType)+1);
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
			    System.out.println(type);
			    for(Building b:buildings)
			    {
			    	if(b.buildingType.equals(type))
			    	{
			    		setDetailsPanel(b);
			    		break;
			    	}
			    }
				if(buildings.size()<=30)
				{
					addIndividualButtons();
				}
				else
				{
					addGroupedButtons(types);
				}
			}
		}
	}
	public void update(Building b)
	{
		if(building!=b)
		{
			building=b;
			setDetailsPanel(b);
		}
		if(b.productionQueue.size()>0&&currentProduction.get(0)!=b.productionQueue.get(0))
		{
			currentProduction.set(0,b.productionQueue.get(0));
			uiButton.allButtons.remove(currentSlot.id);
			gui.rects.remove(currentSlot);
			currentSlot = new uiButton("",100,100){
				@Override 
				public void click()
				{b.cancelProduction(0);}};
			currentSlot.setFillColor(Color.WHITE);
			if(UnitFactory.prototypes.get(b.productionQueue.get(0).unitType).texture!=null)
				currentSlot.setTexture(UnitFactory.prototypes.get(b.productionQueue.get(0).unitType).texture);
			currentSlot.setPosition(800,800);
			gui.rects.add(0,currentSlot);

			progressBar.restart(currentProduction.get(0).initialTime,currentProduction.get(0).initialTime-currentProduction.get(0).timeToComplete);
		}		
		if(b.productionQueue.size()==0)
		{
			currentSlot.disable();
			progressBar.stop();
		}
		int count=0;
		if(b.productionQueue.size()>1)
		{
			for(TimedBehaviour t:b.productionQueue)
			{
				if(count!=0)
				{
					if(b.productionQueue.get(count)!=currentProduction.get(count))
					{
						currentProduction.set(count,b.productionQueue.get(count));
						updateSmallSlot(b,count);
					}
				}
				count++;
			}
		}
		if(count==0)count++;
		for(int i=count;i<currentProduction.size();i++)
		{
			try{slots.getButtons().get(i-1).disable();}catch(NullPointerException ex){}
		}
	}
	public void setDetailsPanel(Building b)
	{
		detailsPanel=new BuildingDetailsPanel(b);
		detailsPanel.setPosition(Vector2f.add(background.getPosition(),new Vector2f(-100,0)));
	}
	public void updateSmallSlot(Building b,int i)
	{
		uiButton.allButtons.remove(slots.getButtons().get(i-1));
		gui.rects.remove(slots.getButtons().get(i-1));
		uiButton button=new StandardButton(i+""){@Override public void click(){b.cancelProduction(i);}};
		button.setFillColor(Color.WHITE);
		if(UnitFactory.prototypes.get(b.productionQueue.get(i).unitType).texture!=null)
			button.setTexture(UnitFactory.prototypes.get(b.productionQueue.get(i).unitType).texture);
		slots.addButton(button, i-1, 0);
		gui.rects.add(0,slots.getButtons().get(i-1));
	}
	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) {
		arg0.draw(background);
		if(buildings.size()==1)
		{
		arg0.draw(progressBar);
		arg0.draw(currentSlot);
		arg0.draw(slots);
		}
		else
			arg0.draw(selectionSlots);
		arg0.draw(detailsPanel);
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
	    				ArrayList<Building> es = new ArrayList<Building>();
	    				for(Building b:buildings)
	    				{
	    					if(b.buildingType.equals(type))
	    						es.add(b);
	    				}
	    				gui.window.activePlayer.selectBuildings(es);
    				}};	
			//b.text.setColor(Color.BLACK);
			selectionSlots.addButton(b,x,y);
        	try{
				b.setTexture(BuildingFactory.prototypes.get(type).texture);
				b.setFillColor(Color.WHITE);
			}
			catch(Exception e){}
        	x++;
	    }
	}
	public void addIndividualButtons()
	{
		int x=0,y=0;
		for(Building b:buildings)
		{
			if(x>9){x=0;y++;}
			addButton(b, x, y);			
			x++;
		}
	}
	public void addButton(Building building,int x,int y)
	{
		uiButton b= new StandardButton(""){
			@Override public void click(){
				gui.window.activePlayer.selectBuilding(building.id);
				}};
				try{
					b.setTexture(BuildingFactory.prototypes.get(building.buildingType).texture);
					b.setFillColor(Color.WHITE);
				}
				catch(Exception e){}
		selectionSlots.addButton(b, x, y);		
	}
	public boolean selectedBuildingsChanged()
	{
		if(buildings.size()!=gui.window.activePlayer.getSelectedBuildings().size())
			return true;
		for(Building b:buildings)
		{
			if(!gui.window.activePlayer.getSelectedBuildings().contains(b))
				return true;
		}
		return false;
	}
}
