package JORTS.behaviour;

import org.jsfml.graphics.CircleShape;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.system.Vector2f;

import JORTS.core.Player;

public interface Attackable {
	public void updateHealth(int dmg);
	public Vector2f getPosition();
	public boolean contains(Vector2f v);
	public float getDistance(CircleShape e);
	public float getDistance(RectangleShape e);
	public Vector2f getTargetPoint(Vector2f v);
	public boolean isActing();
	public boolean isAlive();	
	public Player getOwner();
}
