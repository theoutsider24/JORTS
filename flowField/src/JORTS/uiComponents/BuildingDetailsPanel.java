package JORTS.uiComponents;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;

import JORTS.gameElements.buildings.Building;
import JORTS.uiComponents.textFields.TextField;
import JORTS.uiComponents.textFields.UpdatableTextField;

public class BuildingDetailsPanel extends RectangleShape{
	RectangleShape unitImage;

	HealthBar healthBar;

	UpdatableTextField health;
	TextField title;
	public BuildingDetailsPanel(Building building)
	{
		setSize(new Vector2f(120,200));
		setFillColor(new Color(150,150,150));
		setOutlineColor(new Color(30,30,30));
		setOutlineThickness(2);
		
		unitImage=new RectangleShape();
		unitImage.setSize(new Vector2f(100,100));
		unitImage.setOrigin(-10, -10);
		if(building.getTexture()!=null)unitImage.setTexture(building.getTexture());
	
		healthBar=new HealthBar(building,100);
		healthBar.setOrigin(-10,-120);
		healthBar.healthOverlay.setOrigin(-10,-120);
		
		title=new TextField();
		title.setText(building.buildingType);
		title.setSize(new Vector2f(100,20));
		title.setOrigin(-10,-130);
		title.text.setCharacterSize(15);

		health=new UpdatableTextField(){
			@Override
			public void update()
			{
				health.setText("Health: "+building.health+"/"+building.maxHealth);
			}
		};
		health.setSize(new Vector2f(100,13));
		health.setOrigin(-10,-150);
		health.text.setCharacterSize(10);
		health.update();
	}
	@Override
	public void draw(RenderTarget arg0,RenderStates arg1)
	{
		super.draw(arg0, arg1);
		arg0.draw(unitImage);
		arg0.draw(title);
		arg0.draw(health);
		arg0.draw(healthBar);
	}
	@Override
	public void setPosition(Vector2f v)
	{
		super.setPosition(v);
		unitImage.setPosition(v);
		title.setPosition(v);
		health.setPosition(v);
		healthBar.setPosition(v,true);
	}
	public void update()
	{
		health.update();
		healthBar.update(0);
	}
}
