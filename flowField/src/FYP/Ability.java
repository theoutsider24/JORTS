package FYP;

public abstract class Ability implements Runnable{

	String name;
	public Ability(String name)
	{
		this.name=name;
	}
	@Override
	public abstract void run();
	public String getName()
	{
		return name;
	}

}
