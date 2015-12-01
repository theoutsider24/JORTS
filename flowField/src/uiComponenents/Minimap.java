package uiComponenents;

import static common.Constants.*;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.Image;
import org.jsfml.graphics.Image.PixelOutOfBoundsException;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Mouse;

import FYP.Main;
import FYP.Player;
import units.Entity;

public class Minimap extends RectangleShape{
	Image image;
	Texture texture;
	float sizeFactor=4;
	RectangleShape viewRect;
	public Minimap()
	{
		super();
		viewRect=new RectangleShape();
		viewRect.setFillColor(Color.TRANSPARENT);
		viewRect.setOutlineThickness(1);
		viewRect.setOutlineColor(Color.WHITE);
		
		image = new Image();
		image.create(GRID_SIZE*(int)sizeFactor,GRID_SIZE*(int)sizeFactor);
		texture = new Texture();
		//setSize(new Vector2f(GRID_SIZE*sizeFactor,GRID_SIZE*sizeFactor));
		setSize(new Vector2f(200,200));
		
		setOutlineThickness(3);
		setOutlineColor(new Color(50,50,50));
	}
	@Override 
	public void draw(RenderTarget arg0,RenderStates arg1)
	{
		super.draw(arg0, arg1);
		arg0.draw(viewRect);
	}
	public void update()
	{
		for(int i=0;i<image.getSize().x;i++)
			for(int j=0;j<image.getSize().y;j++)
			{
				Color c= new Color(0,0,0);
				
				if(!Main.worldMap.getCell(i/(int)sizeFactor, j/(int)sizeFactor).isTraversable())
					c=Color.BLACK;
				else if(!Main.worldMap.getCell(i/(int)sizeFactor, j/(int)sizeFactor).getEntities().isEmpty())
					c=Main.worldMap.getCell(i/(int)sizeFactor, j/(int)sizeFactor).getEntities().get(0).player.color;
				else if(Main.worldMap.getCell(i/(int)sizeFactor, j/(int)sizeFactor).isTraversable())
					c=Color.GREEN;
				/*if(Main.worldMap.getCell(i/sizeFactor, j/sizeFactor).getEntities().size()>0)
					c=Color.BLUE;*/
				image.setPixel(i, j, c);
			}
		/*for(Player p:Main.players)
			for(Entity e:p.getUnits())
			{
				try{image.setPixel((int)(e.getPosition().x/(CELL_SIZE/sizeFactor)),(int) (e.getPosition().y/(CELL_SIZE/sizeFactor)), e.getFillColor());}
				catch(PixelOutOfBoundsException ex){continue;}
			}*/
		Vector2f size = Main.gameView.getSize();
		Vector2f newSize = new Vector2f(size.x*(sizeFactor/CELL_SIZE),size.y*(sizeFactor/CELL_SIZE));
		viewRect.setSize(newSize);
		
		Vector2f center = Main.gameView.getCenter();
		center = Vector2f.add(center,Vector2f.div(size,-2));
		
		Vector2f newCenter = new Vector2f(center.x*(sizeFactor/CELL_SIZE),center.y*(sizeFactor/CELL_SIZE));
		newCenter = Vector2f.add(newCenter,getPosition());
		//new Center = Vector2f.add(newCenter, )
		viewRect.setPosition(newCenter);
	//	System.out.println(newCenter.toString());
		
		try {texture.loadFromImage(image);} 
		catch (TextureCreationException e) {e.printStackTrace();}
		setTexture(texture);
		
		Vector2i mousePos = Mouse.getPosition(Main.window);
		Vector2f floatPos = new Vector2f(mousePos.x,mousePos.y);
		if(Mouse.isButtonPressed(Mouse.Button.LEFT)&&Main.gui.cursor.state.contains("minimap"))
		{
			moveCamera(floatPos);
		}
	}
	public void moveCamera(Vector2f uiClickLoc)
	{
		Vector2f loc = Vector2f.sub(uiClickLoc,getPosition());
		Vector2f mapPos = new Vector2f(loc.x/(sizeFactor/CELL_SIZE),loc.y/(sizeFactor/CELL_SIZE));
		Main.gameView.setCenter(mapPos);
		//System.out.println(mapPos.toString());
	}
	public Vector2f getWorldCoords(Vector2f v)
	{
		Vector2f loc = Vector2f.sub(v,getPosition());
		return new Vector2f(loc.x/(sizeFactor/CELL_SIZE),loc.y/(sizeFactor/CELL_SIZE));
	}
	@Override
	public String toString()
	{
		return "minimap";
	}
}
