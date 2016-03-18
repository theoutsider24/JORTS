package uiComponents.textFields;

import java.io.IOException;
import java.nio.file.Paths;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Text;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

import FYP.Main;

public class TextField extends RectangleShape implements Drawable
{
	RectangleShape backgroundRect;
	public Text text;
	String id="";
	static int numInitiated=0;
	boolean centered=false;
	public TextField()
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
		
		/*Texture texture=new Texture();
		try {
			texture.loadFromFile(Paths.get("imgs//border.png"));
			setFillColor(Color.WHITE);
			setTexture(texture);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) {	
		super.draw(arg0, arg1);
		arg0.draw(text);
	}
	
	@Override
	public void setPosition(Vector2f v)
	{
		super.setPosition(v);
		if(centered)
		{
			centerText();
		}
		else
			text.setPosition(v);
	}
	@Override
	public void setOrigin(Vector2f v)
	{
		super.setOrigin(v);
		text.setOrigin(v);
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
	public void setCentered(boolean b)
	{
		centered=b;
		setPosition(getPosition());
	}
	private void centerText()
	{
		FloatRect textbounds = text.getGlobalBounds();
		text.setOrigin(textbounds.width / 2, textbounds.height );
		Vector2f v = Vector2f.add(getPosition(),Vector2f.div(getSize(),2));
		text.setPosition(v);
	}
}
