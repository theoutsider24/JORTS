package JORTS.communication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

import JORTS.common.CommonFunctions;
import JORTS.core.Main;
import JORTS.core.Player;
import JORTS.gameElements.buildings.Building;
import JORTS.gameElements.units.Entity;

public class EventManager  implements Observer
{
	Player player;
	
	int objectCounter=0;
	TreeMap <String,Entity> visibleUnits;
	//ArrayList<Entity> visibleUnits;
	//ArrayList<Building> visibleBuildings;
	TreeMap <String,Building> visibleBuildings;
	
	/*ArrayList<Entity> myUnits;
	ArrayList<Building> myBuildings;*/
	
	Server server;
	String report;
	public EventManager(Player p,Server s)
	{
		this.server=s;
		player=p;
		//visibleUnits=new ArrayList<Entity>();
		visibleUnits = new TreeMap<String,Entity>(String.CASE_INSENSITIVE_ORDER);
		visibleBuildings = new TreeMap<String,Building>(String.CASE_INSENSITIVE_ORDER);
		/*myUnits=new ArrayList<Entity>();
		//visibleBuildings= new ArrayList<Building>();
		myBuildings= new ArrayList<Building>();
		*/
		Main.game.addObserver(this);
	}
	public void update()
	{
		report="";
		getVisibleUnitReport();
		getVisibleBuildingReport();
		report=report.trim();
		//if(!report.equals(""))System.out.println(report);
		if(!report.equals(""))server.sendMessage(player, report);
	}
	public String getVisibleUnitReport()
	{
		Iterator<Map.Entry<String, Entity>> visibleUnitsIterator=visibleUnits.entrySet().iterator();
		String report="";
		while(visibleUnitsIterator.hasNext())
		{
			Map.Entry<String, Entity> entry=visibleUnitsIterator.next();
			Entity e =entry.getValue();
			if(!CommonFunctions.isVisible(e, player))
			{
				addReportString("Lost Sight of Unit: "+entry.getKey()+"\n");
				visibleUnitsIterator.remove();
			}
			else if(e.dead)
			{
				addReportString("Unit died:"+entry.getKey()+", "+e.getPosition()+"\n");
				visibleUnitsIterator.remove();
			}
		}
		
		Iterator<Map.Entry<String, Entity>> it=Entity.allEntities.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<String, Entity> entry=it.next();
			Entity e =entry.getValue();
			if(!visibleUnits.values().contains(e)&&CommonFunctions.isVisible(e, player))
			{
				String newId="Unit#"+objectCounter++;
				addReportString("player:"+e.player.name+" UnitSeen: "+newId +", "+e.getPosition()+"\n");
				visibleUnits.put(newId,e);
			}
		}
		return report;
	}
	
	public String getVisibleBuildingReport()
	{
		String report="";
		Iterator<Map.Entry<String, Building>> visibleBuildingsIterator=visibleBuildings.entrySet().iterator();
		while(visibleBuildingsIterator.hasNext())
		{
			Map.Entry<String, Building> entry=visibleBuildingsIterator.next();
			Building b =entry.getValue();			
			if(!CommonFunctions.isVisible(b, player))
			{
				addReportString("Lost Sight of Building: "+b.buildingType+"\n");
				visibleBuildingsIterator.remove();
			}
			else if(b.destroyed)
			{
				addReportString("Building destroyed :"+b.buildingType+", "+b.getPosition()+"\n");
				visibleBuildingsIterator.remove();
			}
		}
		Iterator<Map.Entry<String, Building>> it=Building.allBuildings.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<String, Building> entry=it.next();
			Building b =entry.getValue();
			if(!visibleBuildings.values().contains(b)&&CommonFunctions.isVisible(b, player))
			{
				String newId="Building#"+objectCounter++;
				addReportString("player:"+b.player.name+" BuildingSeen: "+newId +", "+b.getPosition()+"\n");
				visibleBuildings.put(newId,b);
			}
		}
		return report;
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		update();
	}
	public void addReportString(String s)
	{
		if(!s.trim().equals(""))
			report+=Main.milliseconds+": "+s.trim()+"\n";
	}
}
