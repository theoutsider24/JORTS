package JORTS.uiComponents;

import static JORTS.common.Constants.RESOLUTION_X;
import static JORTS.common.Constants.RESOLUTION_Y;

import java.util.ArrayList;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.PrimitiveType;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Vertex;
import org.jsfml.graphics.VertexArray;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Mouse;

import JORTS.core.GameWindow;
import JORTS.core.Main;
import JORTS.gameElements.buildings.Building;
import JORTS.gameElements.buildings.BuildingFactory;
import JORTS.gameElements.map.MapCell;
import JORTS.gameElements.units.Entity;
import JORTS.gameElements.units.UnitFactory;

public class Cursor extends VertexArray{
	GameWindow window;
	Color color = Color.WHITE;
	float factor=3;
	int xSize=(int)(6f*factor);
	int ySize=(int)(9f*factor);
	private Vector2i position;
	ArrayList<Vertex> startingPosition=new ArrayList<Vertex>();
	
	ArrayList<Vertex> outlineStartingPosition=new ArrayList<Vertex>();
	public String state="";
	
	public String startState="";
	
	VertexArray outline;
	
	public Entity attachedUnit=null;
	public Building attachedBuilding=null;
	
	public Cursor(GameWindow window)
	{
		this.window=window;
		outline = new VertexArray();
		outline.setPrimitiveType(PrimitiveType.LINE_STRIP);
		position=new Vector2i(0,0);
		this.setPrimitiveType(PrimitiveType.TRIANGLES);
		init();
		setPosition(new Vector2i(RESOLUTION_X/2,RESOLUTION_Y/2));
	}
	public void init()
	{
		Vector2f[] pts = new Vector2f[]{
			new Vector2f(0,0),//tip
			new Vector2f(0,ySize),//bottom left
			new Vector2f(xSize*.4f,ySize*.67f),//center
			new Vector2f(xSize,ySize*.67f),//right
			
			new Vector2f(xSize*.27f,ySize*.47f),
			//new Vector2f(xSize*.4f,ySize*.67f),
			new Vector2f(xSize*.5f,ySize),
			new Vector2f(xSize*.79f,ySize*.92f)
		};
		startingPosition.add(new Vertex(pts[0],color));
		startingPosition.add(new Vertex(pts[1],color));
		startingPosition.add(new Vertex(pts[2],color));
		
		startingPosition.add(new Vertex(pts[0],color));
		startingPosition.add(new Vertex(pts[2],color));
		startingPosition.add(new Vertex(pts[3],color));
		
		//startingPosition.add(new Vertex(pts[4],color));
		//startingPosition.add(new Vertex(pts[5],color));
		//startingPosition.add(new Vertex(pts[6],color));
		
		for(Vertex v:startingPosition)
		{
			add(v);
		}
		outlineStartingPosition.add(new Vertex(pts[0],Color.BLACK));
		outlineStartingPosition.add(new Vertex(pts[1],Color.BLACK));
		outlineStartingPosition.add(new Vertex(pts[2],Color.BLACK));
		outlineStartingPosition.add(new Vertex(pts[3],Color.BLACK));
		outlineStartingPosition.add(new Vertex(pts[0],Color.BLACK));
		for(Vertex v:outlineStartingPosition)
		{
			outline.add(v);
		}
		
	}
	@Override
	public void draw(RenderTarget arg0,RenderStates arg1)
	{
		super.draw(arg0, arg1);
		arg0.draw(outline);
	}
	public void move(Vector2f v)
	{
		for(int i=0;i<this.size();i++)
		{
			Vertex vert = get(i);
			set(i,new Vertex(Vector2f.add(vert.position,v),color));
		}
		
		for(int i=0;i<outline.size();i++)
		{
			Vertex vert = outline.get(i);
			outline.set(i,new Vertex(Vector2f.add(vert.position,v),Color.BLACK));
		}
	}
	public void setPosition(Vector2f v)
	{
		for(int i=0;i<this.size();i++)
		{
			set(i,new Vertex(Vector2f.add(startingPosition.get(i).position,v),color));
		}
		for(int i=0;i<outline.size();i++)
		{
			outline.set(i,new Vertex(Vector2f.add(outlineStartingPosition.get(i).position,v),Color.BLACK));
		}
	}
	public void setPosition(Vector2i v)
	{
		position=v;
		setPosition(new Vector2f(v.x,v.y));
	}
	public void setColor(Color c)
	{
		color=c;
		for(int i=0;i<this.size();i++)
		{
			set(i,new Vertex(get(i).position,color));
		}
		
		for(int i=0;i<outline.size();i++)
		{
			outline.set(i,new Vertex(outline.get(i).position,Color.BLACK));
		}
	}
	public Vector2f getUIPosition()
	{
		window.setView(window.uiView);				
		//uiClickLoc =window.mapPixelToCoords(event.asMouseButtonEvent().position);
		Vector2f v =window.mapPixelToCoords(position);
		return v;
	}
	public Vector2f getGamePosition()
	{
		window.setView(window.gameView);	
		Vector2f v =window.mapPixelToCoords(position);
		return v;
	}
	
	public void update()
	{
		if(window.hasFocus)
		{
			Vector2i center = new Vector2i(RESOLUTION_X/2,RESOLUTION_Y/2);
			Vector2i movement= Vector2i.sub(Mouse.getPosition(window),center);
			Mouse.setPosition(Vector2i.add(center,new Vector2i(8,31)));
	
			//Vector2i pos = Mouse.getPosition(window);
			Vector2i pos = Vector2i.add(movement, position);
			
			setPosition(new Vector2i(Math.min(Math.max(pos.x, 0),RESOLUTION_X-3), Math.min(Math.max(pos.y, 0),RESOLUTION_Y-3) ));
		}
		
		/*if(pos.x>0&&pos.x<RESOLUTION_X && pos.y>0&&pos.y<RESOLUTION_Y)
		{
			setPosition(pos);
		}*/
		if(attachedUnit!=null)
			attachedUnit.setPosition(window.mouse.clickLoc);
		
		if(attachedBuilding!=null)
		{
			if(Main.worldMap.cellPosExists(window.mouse.clickLoc))
			{
				MapCell c = Main.worldMap.getCellAtPos(window.mouse.clickLoc);	
				attachedBuilding.origin[0]=c.x;
				attachedBuilding.origin[1]=c.y;
				boolean valid=true;
				
				for(int[] offset:attachedBuilding.offsets)
					if(!Main.worldMap.isCellTraversable(c.x+offset[0], c.y+offset[1]))
						valid=false;
				attachedBuilding.valid=valid;
				for(int[] offset:attachedBuilding.offsets)
				{
					if(attachedBuilding.valid)	
						Main.worldMap.highlightCell(c.x+offset[0], c.y+offset[1], Color.GREEN);
					else
						Main.worldMap.highlightCell(c.x+offset[0], c.y+offset[1], new Color(135,20,20));
				}
				attachedBuilding.setPosition(Main.worldMap.getCell(c.x-(attachedBuilding.dimension/2-1)-1, c.y-(attachedBuilding.dimension/2-1)-1).getPosition());
			}
			else
				attachedBuilding.valid=false;
		}
	}
	public void attachUnit(String type)
	{
		if(attachedUnit==null)
		{
			attachedUnit=UnitFactory.buildEntity(type,0,0,window.activePlayer); 
			attachedUnit.disable();
			window.activePlayer.addUnit(attachedUnit);
			update();
			
			attachedBuilding=null;
		}
		else
			attachedUnit=null;
	}
	
	public void attachUnit(Entity e)
	{
		if(attachedUnit==null)
		{
			attachedUnit=e; 
			attachedUnit.disable();
			window.activePlayer.addUnit(attachedUnit);
			update();
			
			attachedBuilding=null;
		}
		else
			attachedUnit=null;
	}
	public void attachBuilding(String type)
	{
		if(attachedBuilding==null)
		{
			attachedBuilding=BuildingFactory.buildBuilding(type,window.activePlayer); 
			window.activePlayer.addBuilding(attachedBuilding);
			update();
			
			attachedUnit=null;
		}
		else
			attachedBuilding=null;
	}
	public void attachBuilding(Building b)
	{
		if(attachedBuilding==null)
		{
			attachedBuilding=b; 
			window.activePlayer.addBuilding(attachedBuilding);
			update();
			
			attachedUnit=null;
		}
		else
			attachedBuilding=null;
	}
	public void placeAttachedUnit()
	{
		attachedUnit.enable();
		attachedUnit=null;
	}
	public void placeAttachedBuilding()
	{
		if(attachedBuilding.valid)
		{
			attachedBuilding.placeFoundation();
			attachedBuilding.player.issueConstructBuildingOrder(attachedBuilding);		
			attachedBuilding=null;
		}
	}
	public boolean hasUnitAttached()
	{
		return attachedUnit!=null;
	}
	public boolean hasBuildingAttached()
	{
		return attachedBuilding!=null;
	}
	public String getAttachedUnitId()
	{
		if(hasUnitAttached())return attachedUnit.id;
		else	return "no_unit";
	}
	public String getAttachedBuildingId()
	{
		if(hasBuildingAttached())return attachedBuilding.id;
		else	return "no_building";
	}
}
