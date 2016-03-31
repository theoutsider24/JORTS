package JORTS.uiComponents;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Shape;
import org.jsfml.system.Vector2f;

import JORTS.gameElements.buildings.Building;
import JORTS.gameElements.units.Entity;

public class HealthBar extends RectangleShape{
	int maxHealth;
	Object owner;
	String ownerType;
	int currentHealth;
	int width=50;
	RectangleShape healthOverlay=new RectangleShape();
	Color HealthColor=Color.GREEN;
	Color backgroundColor=Color.RED;
	public HealthBar(Entity e,int width)
	{
		super();	
		this.width=width;
		ownerType="entity";
		owner=e;
		maxHealth=e.maxHealth;
		commonInit();
	}
	public HealthBar(Building b,int width)
	{
		super();
		this.width=width;
		ownerType="building";
		owner=b;
		maxHealth=b.maxHealth;
		commonInit();
	}
	public void setToStandardColor()
	{
		backgroundColor=Color.RED;
		HealthColor=Color.GREEN;
		setFillColor(backgroundColor);
		healthOverlay.setFillColor(HealthColor);		
	}
	public HealthBar(Entity e)
	{
		this(e,50);
	}
	public HealthBar(Building b)
	{
		this(b,50);
	}
	public void commonInit()
	{
		setMaxHealth(maxHealth);
		setFillColor(backgroundColor);
		healthOverlay.setFillColor(HealthColor);	
		setSize(new Vector2f(width,4));
		setOrigin(new Vector2f(getSize().x/2,0));
		healthOverlay.setSize(new Vector2f(width,4));
		healthOverlay.setOrigin(new Vector2f(getSize().x/2,0));
		
		setPosition(((Shape)owner).getPosition());

		setOutlineColor(Color.BLACK);
		setOutlineThickness(1);
	}
	public void setToGrayScale()
	{
		backgroundColor=new Color(200,200,200);
		HealthColor=new Color(80,80,80);
		setFillColor(backgroundColor);
		healthOverlay.setFillColor(HealthColor);		
	}
	public void setMaxHealth(int max) {
		maxHealth=max;
		currentHealth=max;
		//width=maxHealth/2;
	}
	@Override
	public void setPosition(Vector2f v)
	{
		if(ownerType.equals("entity"))
		{
			super.setPosition(new Vector2f(v.x,v.y-((Entity)owner).getRadius()-20));
			healthOverlay.setPosition(new Vector2f(v.x,v.y-((Entity)owner).getRadius()-20));
		}
		else 
		{
			super.setPosition(new Vector2f(v.x+((Building)owner).getSize().x/2,v.y-10));
			healthOverlay.setPosition(new Vector2f(v.x+((Building)owner).getSize().x/2,v.y-10));
		}
	}
	public void setPosition(Vector2f v,boolean b)
	{
		super.setPosition(v);
		healthOverlay.setPosition(v);
	}
	/*@Override
	public void setOrigin(float x,float y)
	{
		
	}*/
	public void update(int diff)
	{
		if(ownerType.equals("entity"))
		{
			currentHealth=((Entity)owner).health;
		}
		else
		{
			currentHealth=((Building)owner).health;
		}

		healthOverlay.setSize(new Vector2f(((float)currentHealth/(float)maxHealth)*width,4));
		/*currentHealth+=diff;
		currentHealth=Math.max(0, currentHealth);
		currentHealth=Math.min(maxHealth, currentHealth);
		
		healthOverlay.setSize(new Vector2f(((float)currentHealth/(float)maxHealth)*width,4));*/
	}
	@Override
	public void draw(RenderTarget arg0, RenderStates arg1)
	{
		super.draw(arg0, arg1);
		arg0.draw(healthOverlay);
	}
}
