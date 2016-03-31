package HARTS;

import java.util.Observable;
import java.util.TreeMap;

public class Desires extends Observable{
	public TreeMap<String,Integer> resourceDesires,unitDesires,buildingDesires;
	public boolean initialised=false;
	public Desires()
	{
		resourceDesires= new TreeMap<String,Integer>(String.CASE_INSENSITIVE_ORDER);
		unitDesires= new TreeMap<String,Integer>(String.CASE_INSENSITIVE_ORDER);
		buildingDesires= new TreeMap<String,Integer>(String.CASE_INSENSITIVE_ORDER);

			
		initList(unitDesires,Main.core.queryManager.getGameUnits());
		initList(resourceDesires,Main.core.queryManager.getGameResources());
		initList(buildingDesires,Main.core.queryManager.getGameBuildings());
		initialised=true;
	}
	public void update()
	{
		setChanged();
		notifyObservers();
	}
	public void initList(TreeMap<String,Integer> list,String s)
	{
		String[] types=s.split(",");
		for(int i=0;i<types.length;i++)
		{
			list.put(types[i],1000);
		}
	}
	public String getMostDesiredUnit()
	{
		String unit="";
		int desire=0;
		for(String u:unitDesires.keySet())
		{
			if(unitDesires.get(u)>desire)
			{
				desire=unitDesires.get(u);
				unit=u;
			}
		}
		return unit;
	}
	public void increaseDesire(String s,int i)
	{
		if(resourceDesires.containsKey(s))
			resourceDesires.replace(s,resourceDesires.get(s)+i);
		if(unitDesires.containsKey(s))
			unitDesires.replace(s,unitDesires.get(s)+i);
		if(buildingDesires.containsKey(s))
			buildingDesires.replace(s,buildingDesires.get(s)+i);
	}
}
