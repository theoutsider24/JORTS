package JORTS.uiComponents.grids;

import java.util.ArrayList;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import JORTS.uiComponents.GUI;
import JORTS.uiComponents.buttons.uiButton;

public abstract class ButtonGrid extends RectangleShape implements Drawable{
	uiButton[][] buttons;
	int x,y;
	int spacing=3;
	Vector2i buttonSize;
	GUI gui;
	public ButtonGrid(int x,int y,Vector2f pos,Vector2i size,GUI gui) {
		super();
		this.gui=gui;
		setFillColor(Color.TRANSPARENT);
		setOutlineColor(Color.WHITE);
		setOutlineThickness(1);
		buttonSize=size;
		buttons = new uiButton[x][y];
		setSize(new Vector2f(x*(buttonSize.x+spacing)+spacing,y*(buttonSize.y+spacing)+spacing));
		setPosition(pos);
		this.x=x;
		this.y=y;
	}
	public void addButton(uiButton b,int x,int y)
	{
		b.setSize(new Vector2f(buttonSize.x,buttonSize.y));
		//Main.gui.buttons.add(b);
		buttons[x][y]=b;
		buttons[x][y].setPosition(spacing+getPosition().x+(x*(buttonSize.x+spacing)),spacing+getPosition().y+(y*(buttonSize.y+spacing)));
		gui.rects.add(0,b);
	}
	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) {
		for(int i=0;i<x;i++)
		{
			for(int j=0;j<y;j++)
			{
				try {arg0.draw(buttons[i][j]);}
				catch(Exception ex){continue;}
			}
		}
		super.draw(arg0, arg1);
	}
	public ArrayList<uiButton> getButtons()
	{
		ArrayList<uiButton> buttonList= new ArrayList<uiButton>();
		for(int i=0;i<x;i++)
			for(int j=0;j<y;j++)
				buttonList.add(buttons[i][j]);
		return buttonList;
	}
	@Override
	public String toString()
	{
		return "buttonGrid";
	}
	public String getButtonTitle(Vector2f v)
	{
		for(int i=0;i<x;i++)
		{
			for(int j=0;j<y;j++)
			{
				try {
					if(buttons[i][j].getGlobalBounds().contains(v))
						return buttons[i][j].toString();
				}
				catch(Exception ex){continue;}
			}
		}
		return "";
	}
	public uiButton getButton(Vector2f v)
	{
		for(int i=0;i<x;i++)
		{
			for(int j=0;j<y;j++)
			{
				try {
					if(buttons[i][j].getGlobalBounds().contains(v))
						return buttons[i][j];
				}
				catch(Exception ex){continue;}
			}
		}
		return null;
	}
	public void removeAll()
	{
		for(int i=0;i<x;i++)
		{
			for(int j=0;j<y;j++)
			{
				try{
					uiButton.allButtons.remove(buttons[i][j].title);
					gui.rects.remove(buttons[i][j]);
					buttons[i][j]=null;
				}catch(Exception e){}
			}
		}
	}
	public void disable()
	{
		for(int i=0;i<x;i++)
		{
			for(int j=0;j<y;j++)
			{
				try{
					buttons[i][j].disable();
				}catch(Exception e){}
			}
		}
	}
	public void enable()
	{
		for(int i=0;i<x;i++)
		{
			for(int j=0;j<y;j++)
			{
				try{
					buttons[i][j].enable();
				}catch(Exception e){}
			}
		}
	}
}
