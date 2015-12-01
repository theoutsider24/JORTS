package units;

public class Cavalry extends Entity{
	public Cavalry(int x,int y)
	{
		super(x,y);
		maxSpeed=5;
		setMaxHealth(50);
		unitType="cavalry";
		setRadius(25);
	}
}
