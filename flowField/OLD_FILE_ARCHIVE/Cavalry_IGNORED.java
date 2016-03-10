package units;

public class Cavalry_IGNORED extends Entity{
	public Cavalry_IGNORED(int x,int y)
	{
		super(x,y);
		setUnitType("cavalry");
		
		maxSpeed=5;
		setMaxHealth(50);
		setRadius(25);
	}
}
