package JORTS.behaviour.abilities;

public abstract class Ability implements Runnable{
	public boolean enabled=true;
	public String name;
	public Ability(String name)
	{
		this.name=name;
		this.name=name.replaceAll("_", " ");
	}
	@Override
	public abstract void run();
	public String getName()
	{
		return name;
	}
	public void disable()
	{
		enabled=false;
	}
	public void enable()
	{
		enabled=true;
	}
	public abstract boolean canRun();
}
