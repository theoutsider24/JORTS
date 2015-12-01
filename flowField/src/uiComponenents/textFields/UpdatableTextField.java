package uiComponenents.textFields;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;

import FYP.Main;

public abstract class UpdatableTextField extends RectangleShape implements Drawable
{
	RectangleShape backgroundRect;
	Text text;
	String id="";
	static int numInitiated=0;
	public UpdatableTextField()
	{
		super();
		numInitiated++;
		setId("TEXTFIELD #"+numInitiated);
		setFillColor(Color.WHITE);
		setOutlineColor(Color.BLACK);
		setOutlineThickness(1);
		setSize(new Vector2f(200,50));
		text = new Text("",Main.font);
		text.setCharacterSize(20);
		text.setColor(Color.BLACK);
	}
	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) {	
		super.draw(arg0, arg1);
		arg0.draw(text);
	}
	
	public abstract void update();
	public void setPosition(Vector2f v)
	{
		super.setPosition(v);
		text.setPosition(v);
	}
	public void setText(String s)
	{
		text.setString(s);
	}
	public void setId(String s)
	{
		id=s;
	}
	public String getId()
	{
		return id;
	}
	@Override
	public String toString()
	{
		return getId();
	}
}
