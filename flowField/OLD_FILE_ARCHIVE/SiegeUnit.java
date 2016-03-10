package units;

public class SiegeUnit extends Entity {
	public SiegeUnit(int x,int y)
	{
		super(x,y);
		setUnitType("siege_unit");
		maxSpeed=1;
		setMaxHealth(200);
		setRadius(35);
	}
}
