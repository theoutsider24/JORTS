package units;

public class SiegeUnit extends Entity {
	public SiegeUnit(int x,int y)
	{
		super(x,y);
		maxSpeed=1;
		setMaxHealth(200);
		unitType="siege_unit";
		setRadius(35);
	}
}
