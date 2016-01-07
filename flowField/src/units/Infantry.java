package units;

public class Infantry extends Entity{
	public Infantry(int x,int y)
	{
		super(x,y);
		maxSpeed=2;
		setMaxHealth(100);
		unitType="infantry";
		setRadius(15);
	}
	public Infantry()
	{
		this(0,0);
	}
}
