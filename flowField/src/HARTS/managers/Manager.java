package HARTS.managers;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import HARTS.EventListener;

public abstract class Manager extends Observable implements Observer{
	ArrayList<String> keyWords;
	public Manager(EventListener e,String... keys)
	{
		e.addObserver(this);
		keyWords=new ArrayList<String>();
		for(int i=0;i<keys.length;i++)
		{
			keyWords.add(keys[i]);
		}
	}
	public abstract void processString(String s);
	@Override
	public void update(Observable arg0, Object arg1) {
		String s= ((String)arg1).toLowerCase();
		if(!keyWords.isEmpty())
			for(String key:keyWords)
			{
				if(s.contains(key))
				{
					processString(s);
					break;
				}
			}
	}
}
