package JORTS.uiComponents.buttons;

import java.util.HashMap;

import org.apache.commons.lang3.text.WordUtils;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;

import JORTS.core.Main;

public abstract class uiButton extends RectangleShape{
	public static HashMap<String,uiButton> allButtons= new HashMap<String,uiButton>();
	public String id;
	static int instances;
	public String title;
	public Text text;
	RectangleShape overlay;
	int width=100;
	int height=50;
	boolean drawn=false;
	final int DOWN=0;
	final int UP=1;
	int state=UP;
	boolean togglable;
	boolean toggled;
	public boolean visible=true;
	public Runnable activity=null;
	int charSize =15;
	public uiButton(String title,int x,int y)
	{
		super();
		width=x;
		height=y;
		super.setSize(new Vector2f(width,height));
		setFillColor(Color.BLUE);
		setOutlineThickness(1);
		setOutlineColor(Color.WHITE);
		this.title=title;
		
		overlay=new RectangleShape();
		overlay.setSize(new Vector2f(width,height));
		overlay.setFillColor(new Color(255,255,255,50));
		overlay.setOutlineThickness(1);
		overlay.setOutlineColor(new Color(0,0,0,150));
		
		setTitle(title);
		text.setCharacterSize(charSize);		
		
		text.move((width- text.getGlobalBounds().width)/2,(height- text.getGlobalBounds().height)/2);
		id="button#"+instances++;
		allButtons.put(id,this);
	}
	public uiButton(String title)
	{
		this(title,100,50);
	}
	@Override 
	public void setSize(Vector2f v)
	{
		super.setSize(v);
		overlay.setSize(v);
	}
	public void setTitle(String s)
	{
		text = new Text(s,Main.font);
		text.setCharacterSize(charSize);
		/*if(s.length()>10)
		{
			s=s.replace(" ", "\n");
		}*/
		s=WordUtils.wrap(s,10);
		text.setString(s);
	}
	@Override
	public void setPosition(Vector2f v)
	{
		super.setPosition(v);
		overlay.setPosition(v);
		text.setPosition(v);
	}
	public void clickDown()
	{
		state=DOWN;
	}
	public void clickUp(boolean run)
	{
		
		if(run&&state==DOWN)
		{
			click();
			toggled=!toggled;
		}
		state=UP;
	}
	public void click(){}
	@Override
	public void draw(RenderTarget arg0, RenderStates arg1)
	{
		if(visible)
		{
			super.draw(arg0, arg1);
			arg0.draw(text);
			if (state==DOWN||(togglable&&toggled))
				arg0.draw(overlay);
		}
	}
	public void setTogglable(boolean t)
	{
		togglable=t;
	}
	public void disable()
	{
		visible=false;
	}
	public void enable()
	{
		visible=true;
	}
	@Override
	public String toString()
	{
		if(visible)return id;
		else return "";
	}
}
