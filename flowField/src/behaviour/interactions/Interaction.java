package behaviour.interactions;

import behaviour.timedBehaviours.TimedBehaviour;

public abstract class Interaction extends TimedBehaviour{

	public Interaction(int timeToComplete) {
		super(timeToComplete);
	}
	
}
