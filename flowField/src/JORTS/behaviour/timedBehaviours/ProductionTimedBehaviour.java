package JORTS.behaviour.timedBehaviours;

import java.util.ArrayList;

import JORTS.common.ResourceAmount;
import JORTS.core.GameWindow;
import JORTS.core.Main;
import JORTS.gameElements.buildings.Building;

public class ProductionTimedBehaviour extends TimedBehaviour
{
	public String unitType;
	public Building building;
	public ArrayList<ResourceAmount> requiredResources;
	public ProductionTimedBehaviour(int timeToComplete,String unitType,Building b,ArrayList<ResourceAmount> requiredResources) {
		super(timeToComplete);
		this.unitType=unitType;
		this.requiredResources=requiredResources;
		building=b;
	}
	@Override
	public void run()
	{		
		GameWindow w= Main.getActiveWindow();
		w.runCommand("spawnUnit "+building.id + " " + unitType);
	}
	@Override 
	public void stop()
	{
		super.stop();
	}
}
