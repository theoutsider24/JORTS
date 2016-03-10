package uiComponents;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;

import FYP.Main;

public class ProgressBar implements Drawable{
	int completionTime;
	int timePassed;
	
	RectangleShape container;
	RectangleShape bar;
	Text title;
	float maxBarWidth = 200; 
	boolean completed=false;
	int outlineThickness=2;
	
	public ProgressBar()
	{
		container=new RectangleShape();
		container.setOutlineThickness(outlineThickness);
		container.setOutlineColor(Color.BLACK);
		container.setFillColor(new Color(100,100,100));
		bar=new RectangleShape();
		bar.setFillColor(new Color(200,200,200));
		setSize(200, 40);
		
	}
	public void update()
	{
		if(!completed)
		{
			timePassed+=Main.deltaT;
			float percentFinished = (float)timePassed/(float)completionTime;
			//System.out.println(percentFinished);
			if(percentFinished>=1&&!completed)
			{
				completed=true;
				bar.setSize(new Vector2f(0,bar.getSize().y));
			}
			if(!completed)
				bar.setSize(new Vector2f(maxBarWidth*percentFinished,bar.getSize().y));
		}
		
	}
	public void restart(int completionTime)
	{
		this.completionTime=completionTime;
		completed=false;
		timePassed=0;
	}
	public void restart(int completionTime,int timePassed)
	{
		this.timePassed=timePassed;
		this.completionTime=completionTime;
		completed=false;
	}
	public void stop()
	{
		timePassed=(int) (-1*Main.deltaT);
		update();
		completed=true;
	}
	public void setPosition(float x,float y)
	{
		setPosition(new Vector2f(x,y));
	}
	public void setPosition(Vector2f v)
	{
		container.setPosition(v);
		bar.setPosition(Vector2f.add(v,new Vector2f(outlineThickness,outlineThickness)));
		//title.setPosition(v);
	}
	public void setSize(float x,float y)
	{
		container.setSize(new Vector2f(x,y));
		//bar.setSize(new Vector2f(x-(outlineThickness*2),y-(outlineThickness*2)));
		bar.setSize(new Vector2f(x-(outlineThickness*2),y-(outlineThickness*2)));
		maxBarWidth=x-(outlineThickness*2);
	}
	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) 
	{
		arg0.draw(container);
		arg0.draw(bar);
	}
}
