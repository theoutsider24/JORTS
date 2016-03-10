package FYP;

import static common.Constants.*;

import java.io.IOException;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.View;
import org.jsfml.system.Vector2i;
import org.jsfml.window.ContextSettings;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

import common.Constants;
import gameElements.buildings.BuildingFactory;
import gameElements.units.Entity;
import gameElements.units.UnitFactory;
import uiComponents.GUI;
import uiComponents.CommandLineInterface.CommandInterface;
import uiComponents.CommandLineInterface.Commands;
import uiComponents.uiManagers.KeyboardManager;
import uiComponents.uiManagers.MouseManager;

public class GameWindow extends RenderWindow{
	public boolean editMapMode=false;
	public boolean showFlow=false;
	public View gameView;
	public View uiView;
	public MouseManager mouse;
	public KeyboardManager keyboard;
	public GUI gui;
	public Player activePlayer;
	public boolean hasFocus=true;
	public GameWindow()
	{
		super();
		init();
		gui=new GUI(this);
		activePlayer=Main.players.get(0);
		registerCommands();
		zoom(.25f);
		mouse = new MouseManager(this);
		keyboard=new KeyboardManager(this);
		Main.windows.add(this);
	}
	public GameWindow(Player p)
	{
		this();
		changeActivePlayer(p);
	}
	public void tick() throws IOException
	{
		if(hasFocus)mouse.limitMouse();
		   poll();
		
		clear(new Color(0,0,100));
		
		if(SHOW_VISION_MASK)
			activePlayer.revealMap();
		
		setView(gameView);
		 if(SHOW_VISION_MASK)Main.worldMap.refreshVisionMask();
	   draw(Main.worldMap);
	   if(showFlow)
		   draw(activePlayer.currentField);
	   for(Player p:Main.players)
		   draw(p);
	   if(SHOW_VISION_MASK) draw(Main.worldMap.visionMask);
	   draw(gui);
	   //updateFPSTimer();
	   display();
	}
	public void init()
	{
		ContextSettings settings = new ContextSettings(8);
		int screenmode=RenderWindow.DEFAULT;
		if(Constants.FULLSCREEN)screenmode=RenderWindow.NONE;
		
		VideoMode v = new VideoMode(RESOLUTION_X, RESOLUTION_Y);
		
		if(Constants.FULLSCREEN)v=VideoMode.getDesktopMode();
		create(v, WINDOW_TITLE,screenmode,settings);
		setFramerateLimit(FRAME_CAP);	
		
		setPosition(new Vector2i(0,0));
		setMouseCursorVisible(false);
		
		gameView = new View();
		gameView.setSize(RESOLUTION_X, RESOLUTION_Y);
		
		uiView = new View();
		uiView.setSize(RESOLUTION_X, RESOLUTION_Y);
		uiView.setCenter(RESOLUTION_X/2,RESOLUTION_Y/2);		
	}
	public void poll() throws IOException
	{

		mouse.setMouseLocs();
		mouse.determineHoverIntent();
		for(Event event : pollEvents()) 
		{
			if(event.type == Event.Type.CLOSED) 
	        {
	            close();
	        }
			if(event.type==Event.Type.GAINED_FOCUS)
			{
				Main.takeFocus(this);
				setTitle(WINDOW_TITLE+" (active)");
			}
			if(event.type==Event.Type.LOST_FOCUS)
			{
				hasFocus=false;
				setTitle(WINDOW_TITLE);
			}
			if(hasFocus)	
			{
				mouse.processEvent(event);
		        if(event.type == Event.Type.KEY_PRESSED||event.type == Event.Type.TEXT_ENTERED)
	    		{
		        	keyboard.processKeypress(event);
	    		}
			}
	    }
	}
	public void moveCamera(int x,int y)
	{
		gameView.setCenter(gameView.getCenter().x+x, gameView.getCenter().y+y);
	}
	public void zoom(float zoom)
	{
		gameView.setSize(gameView.getSize().x / zoom, gameView.getSize().y / zoom);
	}
	public void changeActivePlayer(Player p)
	{
		activePlayer=p;
		gui.playerList.update();
		Main.worldMap.resetVision();
	}
	public void registerCommands()
	{
		Commands.registerCommand("zoom_in",new CommandInterface(){
			@Override
			public void run(String[] args) {
				float zoomAmount=1.3f;
				if(args.length>0)
					try{zoomAmount=Float.parseFloat(args[0]);}catch(Exception e){}
				zoom(zoomAmount);
		}});
		Commands.registerCommand("zoom_out",new CommandInterface(){
			@Override
			public void run(String[] args) {
				float zoomAmount=1/1.3f;
				if(args.length>0)
					try{zoomAmount=1/(Float.parseFloat(args[0]));}catch(Exception e){}
				zoom(zoomAmount);
		}});
		Commands.registerCommand("toggle_edit_map",new CommandInterface(){
			@Override
			public void run(String[] args) {
				editMapMode=!editMapMode;
		}});
		Commands.registerCommand("toggle_show_flow",new CommandInterface(){
			@Override
			public void run(String[] args) {
				showFlow=!showFlow;
		}});
		Commands.registerCommand("place_unit",new CommandInterface(){
			@Override
			public void run(String[] args) {
				try{
					String type = "infantry";
					if(args.length==1)
					{
						type=args[0];
					}
					gui.cursor.attachUnit(type);
				}catch(Exception e){}				
		}});
		Commands.registerCommand("addUnit",new CommandInterface(){
			@Override
			public void run(String[] args) {
				try{
					if(args.length==1)
					{
						gui.console.runCommand("place_unit "+args[0]);
						gui.cursor.placeAttachedUnit();
					}
					else if(args.length>=3)
					{
						String type=args[0];
						int x=Integer.parseInt(args[1]);
						int y=Integer.parseInt(args[2]);
						Entity e = UnitFactory.buildEntity(type, x, y, activePlayer);
						activePlayer.addUnit(e);
					}
				}catch(Exception e){}
		}});
		Commands.registerCommand("place_building",new CommandInterface(){
			@Override
			public void run(String[] args) {
				try{
					String type = "barracks";
					if(args.length==1)
					{
						type=args[0];
					}
					gui.cursor.attachBuilding(type);
				}catch(Exception e){}				
		}});
		Commands.registerCommand("addBuilding",new CommandInterface(){
			@Override
			public void run(String[] args) {
				try{
					if(args.length==1)
					{
						gui.console.runCommand("place_building "+args[0]);
						gui.cursor.placeAttachedBuilding();
					}
					else if(args.length>=3)
					{
						String type=args[0];
						int x=Integer.parseInt(args[1]);
						int y=Integer.parseInt(args[2]);
						Entity e = UnitFactory.buildEntity(type, x, y, activePlayer);
						activePlayer.addUnit(e);
					}
				}catch(Exception e){}
		}});
		Commands.registerCommand("exit",new CommandInterface(){
			@Override
			public void run(String[] args) {
				close();
		}});
		Commands.registerCommand("move_camera",new CommandInterface(){
			@Override
			public void run(String[] args) {
				if(args.length>=2)
				{					
					try{
					int x = Integer.parseInt(args[0]);
					int y = Integer.parseInt(args[1]);					
					moveCamera(x,y);
					}catch(Exception e){}	
				}
		}});
		Commands.registerCommand("setPlayer",new CommandInterface(){
			@Override
			public void run(String[] args) {
				try{
					if(args.length>0)
					{
						int pNum = Integer.parseInt(args[0]);
						if(pNum<Main.players.size()&&pNum>=0)
							changeActivePlayer(Main.players.get(pNum));
					}
				}catch(Exception e){}
		}});
		Commands.registerCommand("pause",new CommandInterface(){
			@Override
			public void run(String[] args) {
				PAUSED=!PAUSED;
		}});
		Commands.registerCommand("assignControlGroup",new CommandInterface(){
			@Override
			public void run(String[] args) {
				try{
					if(args.length>0)
					{
						int ctrlGrp = Integer.parseInt(args[0]);
						activePlayer.assignControlGroup(ctrlGrp);
					}
				}catch(Exception e){}
		}});
		Commands.registerCommand("selectControlGroup",new CommandInterface(){
			@Override
			public void run(String[] args) {
				try{
					if(args.length>0)
					{
						int ctrlGrp = Integer.parseInt(args[0]);
						activePlayer.selectControlGroup(ctrlGrp);
					}
				}catch(Exception e){}
		}});
		Commands.registerCommand("openCell",new CommandInterface(){
			@Override
			public void run(String[] args) {
				if(editMapMode)
					Main.worldMap.closeCellsAtPosFour(mouse.clickLoc,true);
		}});
		Commands.registerCommand("saveMap",new CommandInterface(){
			@Override
			public void run(String[] args) {
				if(editMapMode)
					try {
						Main.worldMap.saveToFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
		}});
	}
}
