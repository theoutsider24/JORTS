package behaviour.interactions;

import common.CommonFunctions;
import gameElements.units.Entity;

public class UnitCombatInteraction extends Interaction{

	Entity attacker;
	Entity target;
	public UnitCombatInteraction(int timeToComplete,Entity attacker,Entity target) {
		super(timeToComplete);
		this.attacker=attacker;
		this.target=target;
		start();
		attacker.acting=true;
	}
	@Override
	public void run() {
		target.updateHealth(-10);
		stop();
	}
	@Override
	public void tick()
	{
		if(target.dead||attacker.dead)
			stop();
		else if(CommonFunctions.getDistSqr(attacker.getPosition(),target.getPosition())<10000)
			super.tick();
		else
			stop();

		if(!attacker.acting)
			stop();
	}
	@Override
	public void stop()
	{
		attacker.acting=false;
		super.stop();
	}
}
