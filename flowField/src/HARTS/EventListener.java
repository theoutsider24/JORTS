package HARTS;

import java.util.Observable;

public class EventListener extends Observable{
	public EventListener()
	{
		
	}
	public void reportEvent(String s)
	{
		s=s.trim();
		s=s.toLowerCase();
		String[] events=s.split("\n");
		for(String event:events)
		{
			if(!event.equals(""))
			{
				setChanged();
				this.notifyObservers(event);
			}
		}
	}
}
