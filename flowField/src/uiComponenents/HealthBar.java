package uiComponenents;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.system.Vector2f;

import units.Entity;

public class HealthBar extends RectangleShape{
	int maxHealth;
	Entity owner;
	int currentHealth;
	public HealthBar(Entity e)
	{
		super();
		owner=e;
		maxHealth=e.maxHealth;
		setMaxHealth(maxHealth);
		setFillColor(Color.RED);
	}
	public void setMaxHealth(int max) {
		maxHealth=max;
		setSize(new Vector2f(maxHealth/2,4));
		setOrigin(new Vector2f(getSize().x/2,0));
		setPosition(owner.getPosition());
	}
	@Override
	public void setPosition(Vector2f v)
	{
		super.setPosition(new Vector2f(v.x,v.y-owner.getRadius()-20));
	}
}
