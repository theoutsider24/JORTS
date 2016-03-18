package behaviour.interactions;

import common.CommonFunctions;
import gameElements.buildings.Building;
import gameElements.units.Entity;

public class UnitBuildingCombatInteraction extends Interaction{
	Entity attacker;
	Building target;
	public UnitBuildingCombatInteraction(int timeToComplete,Entity attacker,Building target) {
		super(timeToComplete);
		this.attacker=attacker;
		this.target=target;
		start();
		attacker.acting=true;
	}
	@Override
	public void run() {
		target.updateHealth(-10);		
		int current = attacker.player.resources.get("Wood");
		attacker.player.resources.put("Wood",current+=10);
		attacker.acting=false;
	}
	@Override
	public void tick()
	{
		if(target.destroyed||attacker.dead)
			stop();
		if(CommonFunctions.getDist(target.getGlobalBounds(),attacker.getPosition())-attacker.getRadius()<50)
			super.tick();
		else
		{
			System.out.println("out of range");
			stop();
		}

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
