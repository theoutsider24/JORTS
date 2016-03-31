package JORTS.behaviour.interactions;

import JORTS.behaviour.Attackable;
import JORTS.gameElements.units.Entity;

public class UnitAttackCombatInteraction extends Interaction{
	Entity attacker;
	Attackable target;
	public UnitAttackCombatInteraction(int timeToComplete,Entity attacker,Attackable target) {
		super(timeToComplete);
		this.attacker=attacker;
		this.target=target;
		start();
		attacker.acting=true;
	}
	@Override
	public void run() 
	{
		target.updateHealth(-attacker.damage);		
		//attacker.attackSound.play();
		attacker.acting=false;
	}
	@Override
	public void tick()
	{
		if(!target.isAlive()||attacker.dead)
			stop();
		if(target.getDistance(attacker)<(attacker.attackRange*attacker.attackRange))
			super.tick();
		else
		{
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
