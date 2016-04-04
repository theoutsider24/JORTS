package HARTS.managers;

import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import HARTS.EventListener;
import HARTS.Query;

public class QueryManager extends Manager
{
	ArrayList<Query> queries;
	public QueryManager(EventListener e)
	{
		super(e,"query");
		queries=new ArrayList<Query>();
	}
	public String whatCanIBuild()
	{
		return makeQuery("what can i build?");
	}
	public String getGameUnits()
	{
		return makeQuery("what units are in the game?");
	}
	public String getGameResources()
	{
		return makeQuery("what resources are in the game?");
	}
	public String getPlayerList()
	{
		return makeQuery("who are the players?");
	}
	public String getGameBuildings()
	{
		return makeQuery("what buildings are in the game?");
	}
	public String whatProduces(String unit)
	{
		return makeQuery("what produces \""+unit+"\"?");
	}
	public String getBuildingType(String id)
	{
		return makeQuery("what type of building is \""+id+"\"?");
	}
	public String getUnitType(String id)
	{
		return makeQuery("what type of unit is \""+id+"\"?");
	}
	public String getRequiredResources(String type)
	{
		return makeQuery("what resources are required for \""+type+"\"?");
	}
	public String getCurrentResource(String res)
	{
		return makeQuery("how much \""+res+"\" do i have?");
	}
	public void produceUnit(String type,String buildingId)
	{
		makeQuery("produce unit \""+type+"\" from building \""+buildingId+"\"");
	}
	public String getIdleVillager()
	{
		return makeQuery("get random idle villager");
	}
	public String collectResource(String uId,String resId)
	{
		return makeQuery("\""+uId+"\" collect \""+resId+"\"");
	}
	public String makeQuery(String s)
	{
		Query q=new Query(s);
		queries.add(q);
		
		try {q.join();} 
		catch (InterruptedException e) {e.printStackTrace();}
		return q.response;
	}
	public void registerResponse(String s)
	{
		String id=s.split(":")[0].trim();
		for(int i=0;i<queries.size();i++)
		{
			Query q = queries.get(i);
			if(q.id.equals(id))
			{
				q.setResponse(s.replace(id+":", ""));
				queries.remove(q);
			}
		}
	}
	@Override
	public void processString(String s) {
		registerResponse(s);
	}
}
