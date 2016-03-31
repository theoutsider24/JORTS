package HARTS.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.ml.clustering.*;

import HARTS.EventListener;
import HARTS.Unit;


public class UnitManager extends Manager{
	TreeMap<String,ArrayList<Unit>> units;
	DBSCANClusterer<Unit> clusterer;
	ArrayList<String> buffer;
	public UnitManager(EventListener e)
	{
		super(e,"unit");
		clusterer=new DBSCANClusterer<>(50, 2);
		buffer=new ArrayList<String>();
		units=new TreeMap<String,ArrayList<Unit>>(String.CASE_INSENSITIVE_ORDER);
	}
	public void addPlayer(String id)
	{
		units.put(id, new ArrayList<Unit>());
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
			if(!units.isEmpty())
				addUnit(s);
			else
				buffer.add(s);
		}
	}
	public void addUnit(String s)
	{
		System.out.println(s);
		String id=s.substring(s.indexOf("unitseen:")+9);
		id=id.substring(0, id.indexOf(",")).trim();
		
		String playerName=s.substring(s.indexOf("player:")+7).trim();
		playerName=playerName.substring(0, playerName.indexOf(" ")).trim();
		
		String location=s.substring(s.lastIndexOf("{"));
		location=location.replaceAll("[{}xy=]", "");
		String[] doubles = location.split(",");
		Double f1=Double.parseDouble(doubles[0].trim());
		Double f2=Double.parseDouble(doubles[1].trim());
		units.get(playerName).add(new Unit(id,f1,f2));

		
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
