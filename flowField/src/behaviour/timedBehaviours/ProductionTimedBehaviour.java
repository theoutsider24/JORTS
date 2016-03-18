package behaviour.timedBehaviours;

import gameElements.buildings.Building;
import gameElements.units.Entity;

public abstract class ProductionTimedBehaviour extends TimedBehaviour
{
	public String unitType;
	public ProductionTimedBehaviour(int timeToComplete,String unitType) {
		super(timeToComplete);
		this.unitType=unitType;
	}

}
