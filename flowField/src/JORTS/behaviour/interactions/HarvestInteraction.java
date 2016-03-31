package JORTS.behaviour.interactions;

import JORTS.behaviour.Attackable;
import JORTS.gameElements.buildings.resources.Resource;
import JORTS.gameElements.units.Entity;

public class HarvestInteraction  extends UnitAttackCombatInteraction{

	public HarvestInteraction(int timeToComplete, Entity attacker, Attackable target) {
		super(timeToComplete, attacker, target);
	}
	@Override
	public void run()
	{
		attacker.harvest((Resource)target);
		stop();
	}
}
