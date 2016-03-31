package JORTS.behaviour.timedBehaviours;

import java.util.Observable;
import java.util.Observer;

import JORTS.core.Main;

public abstract class TimedBehaviour implements Runnable, Observer{
	public int timeToComplete;
	public int initialTime;
	public boolean running=false;
	public TimedBehaviour(int timeToComplete)
	{
		this.timeToComplete=timeToComplete;
		this.initialTime=timeToComplete;
	}
	public void tick()
	{
		if(running)
		{
			timeToComplete-=Main.deltaT;
			if(timeToComplete<=0)
			{
				run();
				stop();
			}
		}
	}
	public void start()
	{
		running=true;
		Main.game.addObserver(this);
	}
	public void stop()
	{
		running=false;
		Main.game.deleteObserver(this);
	}
	@Override
	public void update(Observable o, Object arg) {
		tick();		
	}
}
