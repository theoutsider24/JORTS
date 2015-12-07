package FYP;

import uiComponenents.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Properties;

import uiComponenents.grids.*;
import uiComponenents.GUI;
import uiComponenents.Minimap;
import uiComponenents.buttons.*;
import org.jsfml.graphics.*;
import org.jsfml.system.Clock;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.*;
import org.jsfml.window.Mouse.Button;
import org.jsfml.window.event.Event;

import common.CommonFunctions;
import units.Cavalry;
import units.Entity;
import units.Infantry;
import units.SiegeUnit;

import static common.Constants.*;
import map.Map;
import map.MapCell;
import test.Logger;


public class Main extends Observable{
	public static RenderWindow window;
	public static Font font;
	//public static ArrayList<Entity> entities;
	public static ArrayList<Keyboard.Key> numKeys;
	public static int cellsToOpen = 10;
	//public static Player activePlayer,player2,player3,activePlayer;
	public static Player activePlayer;
	public static ArrayList<Player> players;
	public static Map worldMap;
	public static boolean editMapMode=false;
	public static boolean showFlow=false;
	public static View gameView;
	public static View uiView;
	public static Vector2f uiClickLoc;
	public static Vector2f clickLoc;
	public static Clock clickTimer;
	public static boolean doubleClick;
	public static boolean hasFocus=true;
	public static Clock clock;
	public static GUI gui;
	
	public static String hoverIntent="";

	public static Main game;
	public static void main(String[] args) throws IOException
	{
		game = new Main();
		game.init();
	}
	public Main()
	{
		
	}
	public void init() throws IOException
	{
		loadFont(FONT);		
		
		clickTimer=new Clock();
		clickTimer.restart();
		uiClickLoc = new Vector2f(0, 0);
		clickLoc = new Vector2f(0,0);
		
		worldMap = new Map(); 
		worldMap.loadFile(new File(DEFAUL_MAP));
		players=new ArrayList<Player>();
		players.add(new Player());
		players.add(new Player());
		players.add(new Player());
		activePlayer=players.get(0);
		
		/*for(Player p: players)
		{
			addObserver(p);
		}*/
		
		initWindow();
		gui = new GUI(uiView);
		//gui.initButtons();
		
		numKeys = new ArrayList<Keyboard.Key>();
		numKeys.add(Keyboard.Key.NUM0);
		numKeys.add(Keyboard.Key.NUM1);
		numKeys.add(Keyboard.Key.NUM2);
		numKeys.add(Keyboard.Key.NUM3);
		numKeys.add(Keyboard.Key.NUM4);
		numKeys.add(Keyboard.Key.NUM5);
		numKeys.add(Keyboard.Key.NUM6);
		numKeys.add(Keyboard.Key.NUM7);
		numKeys.add(Keyboard.Key.NUM8);
		numKeys.add(Keyboard.Key.NUM9);
		
		//entities = new ArrayList<Entity>();
		for(int i=0;i<STARTING_UNIT_COUNT;i++)			
			players.get(0).addUnit(new Cavalry((int) (players.get(0).startPosition.x+(int)(Math.random()*200)),(int) (players.get(0).startPosition.y+(int)(Math.random()*200))));
		
		for(int i=0;i<STARTING_UNIT_COUNT;i++)			
			players.get(1).addUnit(new Infantry((int) (players.get(1).startPosition.x+(int)(Math.random()*200)),(int) (players.get(1).startPosition.y+(int)(Math.random()*200))));
		
		for(int i=0;i<STARTING_UNIT_COUNT;i++)			
			players.get(2).addUnit(new SiegeUnit((int) (players.get(2).startPosition.x+(int)(Math.random()*200)),(int) (players.get(2).startPosition.y+(int)(Math.random()*200))));
		
		/*for(Entity e:entities)
			activePlayer.addUnit(e);*/
		
		zoom(.25f);
		clock = new Clock();
		
		/*Thread GUIthread = new Thread(new Runnable(){
			@Override
			public void run() {
				while(true)
					 gui.cursor.update();
			}
		});
		GUIthread.start();*/
		
		while(window.isOpen()) 
		{
			gameLoop();
		}
	}
	public void gameLoop() throws IOException
	{
		setChanged();
		notifyObservers();
		
		if(hasFocus)limitMouse();
		   pollEvents();
		   
		  /* for(Player p:players)
			   p.tick();*/
		   window.clear(new Color(0,0,100));
		   window.setView(gameView);
		   window.draw(worldMap);
		   if(showFlow)
			   window.draw(activePlayer.currentField);
		   
		   for(Player p:players)
			   window.draw(p);
		  
		   window.draw(gui);
		   updateFPSTimer();
		   window.display();
	}
	public static void loadFont(String fontName)
	{
		font = new Font();		
		try {
			font.loadFromFile(Paths.get(FONT_DIRECTORY+"\\"+fontName+".ttf"));
		} catch(IOException ex) {
		    System.out.println("Can't find path");
		}
		new Text(".",font,0);//This fixes a bug with the font not loading properly
	}
	public static void initWindow()
	{
		ContextSettings settings = new ContextSettings(8);
		int screenmode=RenderWindow.DEFAULT;
		if(FULLSCREEN)screenmode=RenderWindow.NONE;
		window = new RenderWindow();
		VideoMode v = new VideoMode(RESOLUTION_X, RESOLUTION_Y);
		
		if(FULLSCREEN)v=VideoMode.getDesktopMode();
		window.create(v, WINDOW_TITLE,screenmode,settings);
		window.setFramerateLimit(FRAME_CAP);	
		
		window.setPosition(new Vector2i(0,0));
		window.setMouseCursorVisible(false);
		
		gameView = new View();
		gameView.setSize(RESOLUTION_X, RESOLUTION_Y);
		
		//isometric test
		//gameView.setSize(RESOLUTION_X, (int)(RESOLUTION_Y*2.2));
		//gameView.rotate(45);
		
		uiView = new View();
		uiView.setSize(RESOLUTION_X, RESOLUTION_Y);
		uiView.setCenter(RESOLUTION_X/2,RESOLUTION_Y/2);
	}
	public static void leftButtonDown()
	{	
		gui.cursor.startState=gui.cursor.state;
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
		if(gui.cursor.state.contains("gui"))
		{
			if(gui.cursor.state.contains("button"))
			{
				String name = gui.cursor.state.substring(gui.cursor.state.lastIndexOf("button_")+7);
				if(uiButton.allButtons.containsKey(name))
				{
					uiButton.allButtons.get(name).clickDown();
				}
			}
		}
		else
		{
			if(editMapMode)
			{
				if(Keyboard.isKeyPressed(Keyboard.Key.LALT))
					worldMap.openCellsAtPosFour(clickLoc,true);
				else
					worldMap.closeCellsAtPosFour(clickLoc,true);
			}
			else
				activePlayer.startSelection(clickLoc);
		}
	}
	public static void leftButtonUp()
	{	
		if(gui.cursor.state.contains("gui"))
		{
			if(gui.cursor.state.contains("button")&&gui.cursor.state.equals(gui.cursor.startState))
			{
				String name = gui.cursor.state.substring(gui.cursor.state.lastIndexOf("button_")+7);
				if(uiButton.allButtons.containsKey(name))
				{
					uiButton.allButtons.get(name).clickUp(true);
				}
			}			
		}
		else
		{
			if(!gui.minimap.getGlobalBounds().contains(uiClickLoc))
			{
				if(activePlayer.selectionInProgress)
					activePlayer.endSelection(clickLoc, true);
			}
			else
			{
				if(activePlayer.selectionInProgress)
					activePlayer.endSelection(clickLoc, false);
			}
			doubleClick=false;
		}

		for(uiButton b:uiButton.allButtons.values())
		{
			b.clickUp(false);
		}
	}
	public static void rightButtonDown()
	{
		boolean clickOnButton=false;
		/*or(uiButton b:gui.buttons)
			if(b.getGlobalBounds().contains(uiClickLoc))
			{
				clickOnButton=true;
			}*/

		if(gui.cursor.state.contains("minimap"))
		{
			//minimap.moveCamera(uiClickLoc);
			activePlayer.issueMoveCommand(gui.minimap.getWorldCoords(uiClickLoc));
			clickOnButton=true;
		}
		if(!clickOnButton&&!gui.cursor.state.contains("gui"))
		{
			if(gui.cursor.state.contains("enemy"))
			{
				String id= gui.cursor.state.substring(gui.cursor.state.lastIndexOf("enemy_")+6);
				activePlayer.issueFollowCommand(Entity.allEntities.get(id));
			}
			else
			{
				Thread t=new Thread(new Runnable(){
					@Override
					public void run() {
						activePlayer.issueMoveCommand(clickLoc);
					}},"Move_Command_Thread");
				t.start();
			}
		}
	}
	public static void determineHoverIntent()
	{
		String guiHover=gui.getMouseHover(uiClickLoc);
		if(guiHover!=null)
		{
			gui.cursor.state="gui_"+guiHover;
			gui.cursor.setColor(Color.WHITE);
			return;
		}
		
		MapCell cell = worldMap.getCellAtPos(clickLoc);
		for(Entity e:cell.getEntities())
		{
			if(CommonFunctions.contains(e,clickLoc))
			{
				if(e.player==activePlayer)
				{
					e.hover();
					gui.cursor.setColor(Color.WHITE);
					//gui.cursor.state="SELECT";
					gui.cursor.state="my_"+e.id;
				}
				else
				{
					gui.cursor.setColor(Color.RED);
					//gui.cursor.state="ATTACK";
					gui.cursor.state="enemy_"+e.id;
				}
				return;
			}
		}
		
		if(cell.isTraversable())
		{
			gui.cursor.setColor(Color.WHITE);
			gui.cursor.state="MOVE";
		}
		else
		{
			gui.cursor.setColor(new Color(180,180,180));
			gui.cursor.state="BLOCKED_CELL";
		}
	}
	public static void rightButtonUp()
	{	
		
	}
	public static void setMouseLocs()
	{
		gui.cursor.update();
		window.setView(gameView);				
		//clickLoc =window.mapPixelToCoords(event.asMouseButtonEvent().position);
		clickLoc =window.mapPixelToCoords(gui.cursor.getPosition());//Mouse.getPosition(window));
		window.setView(uiView);				
		//uiClickLoc =window.mapPixelToCoords(event.asMouseButtonEvent().position);
		uiClickLoc =window.mapPixelToCoords(gui.cursor.getPosition());//Mouse.getPosition(window));
	}
	public static void mouseWheelRolled(int delta)
	{
		if(delta>0)
			zoom(1.3f);
		else
			zoom(1/1.3f);
	}
	public static void limitMouse()
	{
		Vector2i pos =  gui.cursor.getPosition();
		if(pos.x-10<=0) moveCamera(-15,0);
		else if(pos.x+10>=RESOLUTION_X) moveCamera(15,0);
		
		if(pos.y-10<=0) moveCamera(0,-15);
		else if(pos.y+10>=RESOLUTION_Y) moveCamera(0,15);
		//Mouse.setPosition(new Vector2i(pos.x,pos.y));
	}
	public static void pollEvents() throws IOException
	{
		setMouseLocs();
		determineHoverIntent();
		for(Event event : window.pollEvents()) 
		{
			if(event.type==Event.Type.GAINED_FOCUS)
				hasFocus=true;
			if(event.type==Event.Type.LOST_FOCUS)
				hasFocus=false;
				
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
	        if(event.type == Event.Type.CLOSED) 
	        {
	            window.close();
	        }
	        if(event.type == Event.Type.KEY_PRESSED)
    		{
	        	switch(event.asKeyEvent().key)
	        	{
	        		case ESCAPE: window.close(); break;
	        		case UP: moveCamera(0,-10); break;
	        		case DOWN: moveCamera(0,10); break;
	        		case LEFT: moveCamera(-10,0); break;
	        		case RIGHT: moveCamera(10,0); break;
	        		case ADD: zoom(2); break;
	        		case F1: activePlayer=players.get(0); break;
	        		case F2: activePlayer=players.get(1); break;
	        		case F3: activePlayer=players.get(2); break;
	        		case SUBTRACT: zoom(.5f); break;
	        		case S: if(!Keyboard.isKeyPressed(Keyboard.Key.LCONTROL))
	        					worldMap.saveToFile();
        					else
	        					worldMap.saveFile(DEFAUL_MAP);	
	        				; break;
	        		case L: worldMap.loadFromFile(); 
	        		case Z: if(Keyboard.isKeyPressed(Keyboard.Key.LCONTROL))
	        				worldMap.undo();
	        			break;
	        		case N: if(Keyboard.isKeyPressed(Keyboard.Key.LCONTROL))
        				worldMap.openAll();
        			break;
	        		case Y: if(Keyboard.isKeyPressed(Keyboard.Key.LCONTROL))
        				worldMap.redo();
        			break;
	        		default: 
	        			if(numKeys.contains(event.asKeyEvent().key))
						{
	        				if(Keyboard.isKeyPressed(Keyboard.Key.LCONTROL))
	        					activePlayer.assignControlGroup(numKeys.indexOf(event.asKeyEvent().key));
	        				else
	        					activePlayer.selectControlGroup(numKeys.indexOf(event.asKeyEvent().key));
						}
						break;       		
	        	}
    		}
	    }
	}
	public static void moveCamera(int x,int y)
	{
		//View view  = (View) window.getView();
		gameView.setCenter(gameView.getCenter().x+x, gameView.getCenter().y+y);
    	//window.setView((ConstView)view);
	}
	public static void updateFPSTimer()
	{
		 window.setTitle(""+(int)(1000.0/clock.getElapsedTime().asMilliseconds()));
		   clock.restart();
	}
	public static void zoom(float zoom)
	{
	//	View view  = (View) window.getView();
		gameView.setSize(gameView.getSize().x / zoom, gameView.getSize().y / zoom);
    	//window.setView((ConstView)view);
	}
}
