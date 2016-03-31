package HARTS.managers;

import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.commons.math3.ml.clustering.DBSCANClusterer;

import HARTS.Building;
import HARTS.EventListener;
import HARTS.Unit;

public class BuildingManager extends Manager{
	public TreeMap<String,ArrayList<Building>> buildings;
	DBSCANClusterer<Building> clusterer;
	ArrayList<String> buffer;
	public BuildingManager(EventListener e)
	{
		super(e,"building");
		clusterer=new DBSCANClusterer<>(50, 2);
		buffer=new ArrayList<String>();
		buildings=new TreeMap<String,ArrayList<Building>>(String.CASE_INSENSITIVE_ORDER);
	}
	public void addPlayer(String id)
	{
		buildings.put(id, new ArrayList<Building>());
	}
	public void addPlayers(String s)
	{
		String[] players = s.split(",");
		for(int i=0;i<players.length;i++)
		{
			addPlayer(players[i]);
		}
		for(int i=0;i<buffer.size();)
		{
			processString(buffer.get(i));
			buffer.remove(i);
		}
	}
	public void processString(String s)
	{
		if(s.contains("spawned")||s.contains("seen"))
		{
			if(!buildings.isEmpty())
				addBuilding(s);
			else
				buffer.add(s);
		}
	}
	public void addBuilding(String s)
	{
		String id=s.substring(s.indexOf("buildingseen:")+13);
		id=id.substring(0, id.indexOf(",")).trim();
		
		String playerName=s.substring(s.indexOf("player:")+7).trim();
		playerName=playerName.substring(0, playerName.indexOf(" ")).trim();
		
		String location=s.substring(s.lastIndexOf("{"));
		location=location.replaceAll("[{}xy=]", "");
		String[] doubles = location.split(",");
		Double f1=Double.parseDouble(doubles[0].trim());
		Double f2=Double.parseDouble(doubles[1].trim());
		
		System.out.println(id);
		if(!buildings.containsKey(playerName))
			System.out.println(playerName+" not found");
		buildings.get(playerName).add(new Building(id,f1,f2));
		
		

		
		/*List<Cluster<Unit>> clusters= clusterer.cluster(units);
		String message="";

		System.out.println("There are "+clusters.size()+" armies");
		int i=0;
		for(Cluster<Unit> c:clusters)
		{
			System.out.println("Army#"+i+" has "+c.getPoints().size()+" units");
		}*/
	}
}
