package JORTS.uiComponents;

import static JORTS.common.Constants.CELL_SIZE;
import static JORTS.common.Constants.GRID_SIZE;
import static JORTS.common.Constants.SHOW_VISION_MASK;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Image;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Mouse;

import JORTS.common.CommonFunctions;
import JORTS.core.GameWindow;
import JORTS.core.Main;

public class Minimap extends RectangleShape{
	GameWindow window;
	Image image;
	Texture texture;
	float sizeFactor=4;
	RectangleShape viewRect;
	public Minimap(GameWindow window)
	{		
		super();
		this.window=window;
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
							/*if(SHOW_VISION_MASK&&!Main.worldMap.getCell(i/(int)sizeFactor, j/(int)sizeFactor).visible&&!Main.worldMap.getCell(i/(int)sizeFactor, j/(int)sizeFactor).mask)
							{
								c=Color.BLACK;
							}
							else
							{*/
					if(!Main.worldMap.getCell(i/(int)sizeFactor, j/(int)sizeFactor).isTraversable())
						c=Color.BLACK;
					else if(!Main.worldMap.getCell(i/(int)sizeFactor, j/(int)sizeFactor).getEntities().isEmpty())
						c=Main.worldMap.getCell(i/(int)sizeFactor, j/(int)sizeFactor).getEntities().get(0).player.color;
					else if(Main.worldMap.getCell(i/(int)sizeFactor,	j/(int)sizeFactor).isTraversable())
						c=Color.GREEN;
				//}
								/*if(SHOW_VISION_MASK&&Main.worldMap.getCell(i/(int)sizeFactor, j/(int)sizeFactor).mask)
								{
									c=CommonFunctions.substract(c, new Color(150,150,150));
								}*/
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
		Vector2f size = window.gameView.getSize();
		Vector2f newSize = new Vector2f(size.x*(sizeFactor/CELL_SIZE),size.y*(sizeFactor/CELL_SIZE));
		//Vector2f newSize = new Vector2f(size.x*(.02f),size.y*(.02f));
		viewRect.setSize(newSize);
		
		Vector2f center = window.gameView.getCenter();
		center = Vector2f.add(center,Vector2f.div(size,-2));
		
		Vector2f newCenter = Vector2f.mul(center, (sizeFactor/CELL_SIZE));
		
		//Vector2f newCenter = new Vector2f(center.x*(sizeFactor/CELL_SIZE),center.y*(sizeFactor/CELL_SIZE));
		newCenter = Vector2f.add(newCenter,getPosition());
		
		newCenter = Vector2f.sub(newCenter, Vector2f.div(newSize, 2));
		
		viewRect.setPosition(newCenter.x+(viewRect.getSize().x/2),newCenter.y+(viewRect.getSize().y/2));
	//	System.out.println(newCenter.toString());
		
		try {texture.loadFromImage(image);} 
		catch (TextureCreationException e) {e.printStackTrace();}
		setTexture(texture);
		
		Vector2i mousePos = Mouse.getPosition(window);
		Vector2f floatPos = new Vector2f(mousePos.x,mousePos.y);
		if(Mouse.isButtonPressed(Mouse.Button.LEFT)&&window.gui.cursor.state.contains("minimap"))
		{
			moveCamera(floatPos);
		}
	}
	public void moveCamera(Vector2f uiClickLoc)
	{
		Vector2f loc = Vector2f.sub(uiClickLoc,getPosition());
		Vector2f mapPos = new Vector2f(loc.x/(sizeFactor/CELL_SIZE),loc.y/(sizeFactor/CELL_SIZE));
		window.gameView.setCenter(mapPos);
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
