package uiComponents;

import static common.Constants.*;

import org.jsfml.graphics.Color;
import org.jsfml.system.Clock;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Mouse.Button;
import org.jsfml.window.event.Event;

import FYP.Main;
import buildings.Building;
import common.CommonFunctions;
import map.MapCell;
import uiComponents.buttons.uiButton;
import units.Entity;

public class MouseManager {
	public static Vector2f uiClickLoc;
	public static Vector2f clickLoc;
	public static Clock clickTimer;
	public static boolean doubleClick;
	
	public MouseManager()
	{
		uiClickLoc = new Vector2f(0, 0);
		clickLoc = new Vector2f(0,0);
		clickTimer=new Clock();
		clickTimer.restart();
	}
	public void processEvent(Event event)
	{
		if(event.type == Event.Type.MOUSE_BUTTON_PRESSED)
		{
			if(event.asMouseButtonEvent().button==Button.LEFT)		
			{
				leftButtonDown();
			}
			else if(event.asMouseButtonEvent().button==Button.RIGHT)		
			{
				rightButtonDown();
			}
		}
		if(event.type == Event.Type.MOUSE_BUTTON_RELEASED)
		{
			 if(event.asMouseButtonEvent().button==Button.LEFT)
			{
				 leftButtonUp();
			}
			else if(event.asMouseButtonEvent().button==Button.RIGHT)
			{
				rightButtonUp();
			}
		}
		if(event.type == Event.Type.MOUSE_WHEEL_MOVED)
		{
			mouseWheelRolled(event.asMouseWheelEvent().delta);
		}
	}
	public void determineHoverIntent()
	{
		String guiHover=Main.gui.getMouseHover(uiClickLoc);
		if(guiHover!=null)
		{
			Main.gui.cursor.state="gui_"+guiHover;
			Main.gui.cursor.setColor(Color.WHITE);
			return;
		}

		if(Main.gui.cursor.attachedUnit!=null)
		{
			Main.gui.cursor.state= "unitAttached_"+Main.gui.cursor.attachedUnit.id;
			return;
		}
		
		if(Main.gui.cursor.attachedBuilding!=null)
		{
			Main.gui.cursor.state= "buildingAttached_"+Main.gui.cursor.attachedBuilding.id;
			return;
		}
		
		MapCell cell = Main.worldMap.getCellAtPos(clickLoc);
		for(Entity e:cell.getEntities())
		{
			if(CommonFunctions.contains(e,clickLoc))
			{
				if(e.player==Main.activePlayer)
				{
					e.hover();
					Main.gui.cursor.setColor(Color.WHITE);
					//gui.cursor.state="SELECT";
					Main.gui.cursor.state="my_"+e.id;
				}
				else
				{
					Main.gui.cursor.setColor(Color.RED);
					//gui.cursor.state="ATTACK";
					Main.gui.cursor.state="enemy_"+e.id;
				}
				return;
			}
		}
		if(cell.getBuildings().size()>0)
		{
			Building b = cell.getBuildings().get(0);
			if(b.player==Main.activePlayer)
			{
				b.hover();
				Main.gui.cursor.setColor(Color.WHITE);
				//gui.cursor.state="SELECT";
				Main.gui.cursor.state="my_"+b.id;
			}
			else
			{
				Main.gui.cursor.setColor(Color.RED);
				//gui.cursor.state="ATTACK";
				Main.gui.cursor.state="enemy_"+b.id;
			}
			return;
		}
		if(cell.isTraversable())
		{
			Main.gui.cursor.setColor(Color.WHITE);
			Main.gui.cursor.state="MOVE";
		}
		else
		{
			Main.gui.cursor.setColor(new Color(180,180,180));
			Main.gui.cursor.state="BLOCKED_CELL";
		}
	}public void leftButtonDown()
	{	
		Main.gui.cursor.startState=Main.gui.cursor.state;
		if(clickTimer.getElapsedTime().asMilliseconds()<CLICK_INTERVAL)
		{
			doubleClick=true;
		}
		clickTimer.restart();
		
		boolean clickOnButton=false;		
		
		clickTimer.restart();
		
		/*for(uiButton b:gui.buttons)
			if(b.getGlobalBounds().contains(uiClickLoc))
			{
				b.clickDown();
				clickOnButton=true;
			}*/
		if(Main.gui.cursor.state.contains("gui"))
		{
			if(Main.gui.cursor.state.contains("button"))
			{
				String name = Main.gui.cursor.state.substring(Main.gui.cursor.state.lastIndexOf("button_")+7);
				if(uiButton.allButtons.containsKey(name))
				{
					uiButton.allButtons.get(name).clickDown();
				}
			}
		}
		else
		{
			if(Main.editMapMode)
			{
				if(Keyboard.isKeyPressed(Keyboard.Key.LALT))
					Main.worldMap.openCellsAtPosFour(clickLoc,true);
				else
					Main.worldMap.closeCellsAtPosFour(clickLoc,true);
			}
			else if(Main.gui.cursor.state.contains("unitAttached"))
			{
				Main.gui.cursor.attachedUnit.enable();
				Main.gui.cursor.attachedUnit=null;
			}
			else if(Main.gui.cursor.state.contains("buildingAttached"))
			{
				if(Main.gui.cursor.attachedBuilding.valid)
				{
					Main.gui.cursor.attachedBuilding.place();
					Main.gui.cursor.attachedBuilding=null;
				}
			}
			else
				Main.activePlayer.startSelection(clickLoc);
		}
	}
	public void leftButtonUp()
	{	
		if(Main.gui.cursor.state.contains("gui"))
		{
			if(Main.gui.cursor.state.contains("button")&&Main.gui.cursor.state.equals(Main.gui.cursor.startState))
			{
				String name =Main. gui.cursor.state.substring(Main.gui.cursor.state.lastIndexOf("button_")+7);
				if(uiButton.allButtons.containsKey(name))
				{
					uiButton.allButtons.get(name).clickUp(true);
				}
			}			
		}
		else
		{
			if(!Main.gui.minimap.getGlobalBounds().contains(uiClickLoc))
			{
				if(Main.activePlayer.selectionInProgress)
					Main.activePlayer.endSelection(clickLoc, true);
			}
			else
			{
				if(Main.activePlayer.selectionInProgress)
					Main.activePlayer.endSelection(clickLoc, false);
			}
		}

		for(uiButton b:uiButton.allButtons.values())
		{
			b.clickUp(false);
		}

		doubleClick=false;
	}
	public void rightButtonDown()
	{
		boolean clickOnButton=false;
		/*or(uiButton b:gui.buttons)
			if(b.getGlobalBounds().contains(uiClickLoc))
			{
				clickOnButton=true;
			}*/

		if(Main.gui.cursor.state.contains("minimap"))
		{
			//minimap.moveCamera(uiClickLoc);
			Main.activePlayer.issueMoveCommand(Main.gui.minimap.getWorldCoords(uiClickLoc));
			clickOnButton=true;
		}
		if(!clickOnButton&&!Main.gui.cursor.state.contains("gui"))
		{
			if(Main.gui.cursor.state.contains("enemy"))
			{
				String id= Main.gui.cursor.state.substring(Main.gui.cursor.state.lastIndexOf("enemy_")+6);
				Main.activePlayer.issueFollowCommand(Entity.allEntities.get(id));
			}
			else
			{
				Thread t=new Thread(new Runnable(){
					@Override
					public void run() {
						Main.activePlayer.issueMoveCommand(clickLoc);
					}},"Move_Command_Thread");
				t.start();
			}
		}
	}
	
	public void rightButtonUp()
	{	
		
	}
	public void setMouseLocs()
	{
		Main.gui.cursor.update();
		Main.window.setView(Main.gameView);				
		//clickLoc =window.mapPixelToCoords(event.asMouseButtonEvent().position);
		clickLoc =Main.window.mapPixelToCoords(Main.gui.cursor.getPosition());//Mouse.getPosition(window));
		Main.window.setView(Main.uiView);				
		//uiClickLoc =window.mapPixelToCoords(event.asMouseButtonEvent().position);
		uiClickLoc =Main.window.mapPixelToCoords(Main.gui.cursor.getPosition());//Mouse.getPosition(window));
	}
	public void mouseWheelRolled(int delta)
	{
		if(delta>0)
			Main.zoom(1.3f);
		else
			Main.zoom(1/1.3f);
	}
	public void limitMouse()
	{
		Vector2i pos =  Main.gui.cursor.getPosition();
		if(pos.x-BORDER_DRAG_TOLERANCE<=0) Main.moveCamera(-15,0);
		else if(pos.x+10>=RESOLUTION_X) Main.moveCamera(15,0);
		
		if(pos.y-BORDER_DRAG_TOLERANCE<=0) Main.moveCamera(0,-15);
		else if(pos.y+10>=RESOLUTION_Y) Main.moveCamera(0,15);
		//Mouse.setPosition(new Vector2i(pos.x,pos.y));
	}
}
