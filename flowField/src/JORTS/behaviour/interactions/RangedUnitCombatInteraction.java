package JORTS.behaviour.interactions;

import JORTS.behaviour.Attackable;
import JORTS.gameElements.units.Entity;

public class RangedUnitCombatInteraction extends UnitAttackCombatInteraction {
	
	public RangedUnitCombatInteraction(int timeToComplete, Entity attacker, Attackable target) {
		super(timeToComplete, attacker, target);
		if(!attacker.projectileLoaded)
			stop();
	}
	@Override
	public void run()
	{
		attacker.fireProjectile(target);
		stop();
	}
}
