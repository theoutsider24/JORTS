package JORTS.behaviour.interactions;

import JORTS.behaviour.timedBehaviours.TimedBehaviour;

public abstract class Interaction extends TimedBehaviour{

	public Interaction(int timeToComplete) {
		super(timeToComplete);
	}
	
}
