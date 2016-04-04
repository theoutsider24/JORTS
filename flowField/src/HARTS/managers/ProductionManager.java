package HARTS.managers;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import HARTS.Building;
import HARTS.EventListener; 
import HARTS.Main;
import HARTS.Unit;

public class ProductionManager extends Manager{
	public TreeMap<String,Integer> resources;
	public ProductionManager(EventListener e)
	{
		super(e);
		resources=new TreeMap<String,Integer>(String.CASE_INSENSITIVE_ORDER);
		String resString=Main.core.queryManager.getGameResources();
		String[] types=resString.split(",");
		for(int i=0;i<types.length;i++)
		{
			resources.put(types[i],0);
		}

		Timer timer = new Timer();
		TimerTask resUpdater = new TimerTask() {
		    @Override
		    public void run() {
		    	if(!Main.core.isAlive())
		    	{
		    		this.cancel();
		    	}
		    	else
		    	{
		    		for(String r:resources.keySet())
		    		{
		    			resources.replace(r,Integer.parseInt(Main.core.queryManager.getCurrentResource(r).trim()));
		    		}
		    		setChanged();
		    		notifyObservers();
		    	}
		    }
		};

		timer.schedule(resUpdater, 0, 1000);
		TimerTask myTask = new TimerTask() {
		    @Override
		    public void run() {
		    	if(!Main.core.isAlive())
		    	{
		    		this.cancel();
		    	}
		    	else
		    	{
		    		if(Main.core.desires.initialised)
		    		{
		    			requestUnit(Main.core.desires.getMostDesiredUnit());
		    			String freeUnit=Main.core.queryManager.getIdleVillager();
		    			if(!freeUnit.equals(""))
		    			{
		    				Main.core.queryManager.collectResource(freeUnit, getNearestResource(""));
		    			}
		    		}
		    	}
		    }
		};
		timer.schedule(myTask, 100, 100);
	}
	public String getNearestResource(String u)//Random
	{
		ArrayList<Building> resources =Main.core.buildingManager.buildings.get("gaia");
		Building b=resources.get((int)(Math.random()*resources.size()));
		
		return b.id;
	}
	public void requestBuilding(String type)
	{
		String possibleBuildings=Main.core.queryManager.whatCanIBuild().toLowerCase();
		if(possibleBuildings.contains(type.toLowerCase()))
		{
			System.out.println(Main.core.queryManager.getRequiredResources(type.toLowerCase()));
		}
	}
	public void requestUnit(String type)
	{
		System.out.println(type+" requested" );
		String requiredBuilding=Main.core.queryManager.whatProduces(type);
		System.out.println(requiredBuilding+" required to build "+type);
		boolean found=false;
		for(Building b:Main.core.buildingManager.buildings.get(Main.core.playerName))
		{
			if(b.type.toLowerCase().equals(requiredBuilding.toLowerCase()))
			{
				System.out.println("I have the required Buildings");
				String cost=Main.core.queryManager.getRequiredResources(type.toLowerCase());
				String[] costs = cost.split(",");
				boolean canAfford=true;
				for(String s:costs)
				{
					String res=s.split(":")[0];
					int amount=Integer.parseInt(s.split(":")[1]);
					if(resources.get(res)<amount)
						canAfford=false;
				}
				if(canAfford)
				{
					Main.core.queryManager.produceUnit(type,b.id);
				}
						
				found=true;
			}
		}
		if(!found)
		{
			requestBuilding(requiredBuilding);
		}
	}
	@Override
	public void processString(String s) {
		
	}

}
