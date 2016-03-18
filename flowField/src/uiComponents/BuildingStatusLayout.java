package uiComponents;

import java.util.ArrayList;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import behaviour.timedBehaviours.TimedBehaviour;
import gameElements.buildings.Building;
import gameElements.units.Entity;
import gameElements.units.UnitDefinition;
import gameElements.units.UnitFactory;
import uiComponents.buttons.StandardButton;
import uiComponents.buttons.uiButton;
import uiComponents.grids.ButtonGrid;

public class BuildingStatusLayout implements Drawable{
	GUI gui;
	public static ProgressBar progressBar;
	RectangleShape background;
	public ButtonGrid slots;
	public uiButton currentSlot;
	public BuildingDetailsPanel detailsPanel;
	ArrayList<TimedBehaviour> currentProduction;
	Building building;
	
	public BuildingStatusLayout(GUI g){
		currentProduction=new ArrayList<TimedBehaviour>();
		currentProduction.add(null);
		currentProduction.add(null);
		currentProduction.add(null);
		currentProduction.add(null);
		currentProduction.add(null);
		currentProduction.add(null);
		
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
	}
	public void update(ArrayList<Building> buildings)
	{
		if(buildings.size()==1)
			update(buildings.get(0));
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
			slots.getButtons().get(i-1).disable();
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
		arg0.draw(progressBar);
		arg0.draw(currentSlot);
		arg0.draw(slots);
		arg0.draw(detailsPanel);
	}
}
