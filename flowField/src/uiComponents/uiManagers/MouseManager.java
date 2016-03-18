package uiComponents.uiManagers;

import static common.Constants.*;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.system.Clock;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Mouse.Button;
import org.jsfml.window.event.Event;

import FYP.GameWindow;
import FYP.Main;
import common.CommonFunctions;
import gameElements.buildings.Building;
import gameElements.map.MapCell;
import gameElements.units.Entity;
import uiComponents.buttons.uiButton;

public class MouseManager {
	public Vector2f uiClickLoc;
	public Vector2f clickLoc;
	public Clock clickTimer;
	public boolean doubleClick;
	public GameWindow window;
	public MouseManager(GameWindow window)
	{
		this.window=window;
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

		MapCell cell = Main.worldMap.getCellAtPos(clickLoc);
		String guiHover=window.gui.getMouseHover(uiClickLoc);
		if(guiHover!=null)
		{
			window.gui.cursor.state="gui_"+guiHover;
			window.gui.cursor.setColor(Color.WHITE);
			return;
		}

		if(window.gui.cursor.hasUnitAttached())
		{
			window.gui.cursor.state= "unitAttached_"+window.gui.cursor.getAttachedUnitId();
			return;
		}
		
		if(window.gui.cursor.hasBuildingAttached())
		{
			window.gui.cursor.state= "buildingAttached_"+window.gui.cursor.getAttachedBuildingId();
			return;
		}
		for(Entity e:cell.getEntities())
		{
			if(CommonFunctions.contains(e,clickLoc))
			{
				if(e.player==window.activePlayer)
				{
					e.hover();
					window.gui.cursor.setColor(Color.WHITE);
					//gui.cursor.state="SELECT";
					window.gui.cursor.state="my_"+e.id;
				}
				else
				{
					window.gui.cursor.setColor(Color.RED);
					//gui.cursor.state="ATTACK";
					window.gui.cursor.state="enemy_"+e.id;
				}
				return;
			}
		}
		if(cell.getBuildings().size()>0)
		{
			Building b = cell.getBuildings().get(0);
			if(b.player==window.activePlayer)
			{
				b.hover();
				window.gui.cursor.setColor(Color.WHITE);
				//gui.cursor.state="SELECT";
				window.gui.cursor.state="my_"+b.id;
			}
			else
			{
				window.gui.cursor.setColor(Color.RED);
				//gui.cursor.state="ATTACK";
				window.gui.cursor.state="enemy_"+b.id;
			}
			return;
		}
		if(cell.isTraversable())
		{
			window.gui.cursor.setColor(Color.WHITE);
			window.gui.cursor.state="MOVE";
		}
		else
		{
			window.gui.cursor.setColor(new Color(180,180,180));
			window.gui.cursor.state="BLOCKED_CELL";
		}
	}
	public void leftButtonDown()	
	{	
		window.gui.cursor.startState=window.gui.cursor.state;
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
		if(window.gui.cursor.state.contains("gui"))
		{
			if(window.gui.cursor.state.contains("button"))
			{
				//String name = window.gui.cursor.state.substring(window.gui.cursor.state.lastIndexOf("button_")+7);
				String name=window.gui.cursor.state.substring(4);
				//System.out.println(name);
				if(uiButton.allButtons.containsKey(name))
				{
					uiButton.allButtons.get(name).clickDown();
				}
			}
		}
		else
		{
			if(window.editMapMode)
			{
				if(Keyboard.isKeyPressed(Keyboard.Key.LALT))
					Main.worldMap.openCellsAtPosFour(clickLoc,true);
				else
					Main.worldMap.closeCellsAtPosFour(clickLoc,true);
			}
			else if(window.gui.cursor.state.contains("unitAttached"))
			{
				window.gui.cursor.placeAttachedUnit();
			}
			else if(window.gui.cursor.state.contains("buildingAttached"))
			{
				window.gui.cursor.placeAttachedBuilding();
			}
			else
				window.activePlayer.startSelection(clickLoc,window);
		}
	}
	public void leftButtonUp()
	{	
		if(window.gui.cursor.state.contains("gui"))
		{
			if(window.gui.cursor.state.contains("button")&&window.gui.cursor.state.equals(window.gui.cursor.startState))
			{
				//String name =window.gui.cursor.state.substring(window.gui.cursor.state.lastIndexOf("button_")+7);
				String name=window.gui.cursor.state.substring(4);
				if(uiButton.allButtons.containsKey(name))
				{
					uiButton.allButtons.get(name).clickUp(true);
				}
			}	
			if(window.activePlayer.selectionInProgress)
				window.activePlayer.endSelection(clickLoc, false,window);
		}
		else
		{
			if(!window.gui.minimap.getGlobalBounds().contains(uiClickLoc))
			{
				if(window.activePlayer.selectionInProgress)
					window.activePlayer.endSelection(clickLoc, true,window);
			}
			else
			{
				if(window.activePlayer.selectionInProgress)
					window.activePlayer.endSelection(clickLoc, false,window);
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
		if(window.gui.cursor.state.contains("minimap"))
		{
			window.activePlayer.issueMoveCommand(window.gui.minimap.getWorldCoords(uiClickLoc));
			clickOnButton=true;
		}
		if(!clickOnButton&&!window.gui.cursor.state.contains("gui"))
		{
			if(window.activePlayer.getSelectedUnits().size()>0)
			{
				if(window.gui.cursor.state.contains("enemy")&&window.gui.cursor.state.contains("unit"))
				{
					String id= window.gui.cursor.state.substring(window.gui.cursor.state.lastIndexOf("enemy_")+6);
					window.activePlayer.issueFollowCommand(Entity.allEntities.get(id));
				}
				else if(window.gui.cursor.state.contains("enemy")&&window.gui.cursor.state.contains("building"))
				{
					String id= window.gui.cursor.state.substring(window.gui.cursor.state.lastIndexOf("enemy_")+6);
					window.activePlayer.issueAttackBuildingOrder(Building.allBuildings.get(id));
				}
				else if(window.gui.cursor.state.contains("MOVE"))
				{
					Thread t=new Thread(new Runnable(){
						@Override
						public void run() {
							window.activePlayer.issueMoveCommand(clickLoc);
						}},"Move_Command_Thread");
					t.start();
				}
			}
			else if(window.activePlayer.getSelectedBuildings().size()>0)
			{
				window.activePlayer.issueRallyPointOrder(clickLoc);
			}
		}
	}
	
	public void rightButtonUp()
	{	
		
	}
	public void setMouseLocs()
	{
		window.gui.cursor.update();				
		//clickLoc =window.mapPixelToCoords(event.asMouseButtonEvent().position);
		clickLoc =window.gui.cursor.getGamePosition();//Mouse.getPosition(window));
					
		//uiClickLoc =window.mapPixelToCoords(event.asMouseButtonEvent().position);
		uiClickLoc =window.gui.cursor.getUIPosition();//Mouse.getPosition(window));
		
	}
	public void mouseWheelRolled(int delta)
	{
		if(delta>0)
			window.zoom(1.3f);
		else
			window.zoom(1/1.3f);
	}
	public void limitMouse()
	{
		Vector2f pos = window.gui.cursor.getUIPosition();
		if(pos.x-BORDER_DRAG_TOLERANCE<=0) window.moveCamera(-15,0);
		else if(pos.x+BORDER_DRAG_TOLERANCE>=RESOLUTION_X) window.moveCamera(15,0);
		
		if(pos.y-BORDER_DRAG_TOLERANCE<=0) window.moveCamera(0,-15);
		else if(pos.y+BORDER_DRAG_TOLERANCE>=RESOLUTION_Y) window.moveCamera(0,15);
		//Mouse.setPosition(new Vector2i(pos.x,pos.y));
	}
}
