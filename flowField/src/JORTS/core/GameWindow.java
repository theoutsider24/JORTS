package JORTS.core;

import static JORTS.common.Constants.*;

import java.io.IOException;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.View;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.ContextSettings;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

import JORTS.behaviour.timedBehaviours.TimedBehaviour;
import JORTS.common.Constants;
import JORTS.gameElements.AnimationManager;
import JORTS.gameElements.buildings.Building;
import JORTS.gameElements.buildings.BuildingFactory;
import JORTS.gameElements.buildings.resources.Resource;
import JORTS.gameElements.map.MapCell;
import JORTS.gameElements.projectiles.AbstractProjectile;
import JORTS.gameElements.units.Entity;
import JORTS.gameElements.units.UnitFactory;
import JORTS.uiComponents.GUI;
import JORTS.uiComponents.CommandLineInterface.CommandInterface;
import JORTS.uiComponents.CommandLineInterface.Commands;
import JORTS.uiComponents.uiManagers.KeyboardManager;
import JORTS.uiComponents.uiManagers.MouseManager;

public class GameWindow extends RenderWindow{
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
		
		clear(new Color	(30,15,0));
		
		if(SHOW_VISION_MASK)
			activePlayer.revealMap();
		
		setView(gameView);
	 draw(Main.worldMap);
	 if(SHOW_FLOW)
		 draw(activePlayer.currentField);
	 for(Player p:Main.players)
		 draw(p);
	 	draw(AnimationManager.man);
	 if(gui.cursor.attachedBuilding!=null)
		if(gui.cursor.attachedBuilding.valid)
		{
			//draw(gui.cursor.attachedBuilding);
		}
	 for(AbstractProjectile p:AbstractProjectile.allProjectiles)
		 draw(p);
	 
	 if(SHOW_VISION_MASK) draw(activePlayer.visionMap);
	 	draw(gui);
	 //updateFPSTimer();
	 display();
	}
	public void setErrorMessage(String s)
	{
		gui.setErrorMessage(s);
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
		gameView.move(x,y);
		//gameView.setCenter(gameView.getCenter().x+x, gameView.getCenter().y+y);
	}
	public void zoom(float zoom)
	{
		float currentZoom = RESOLUTION_Y/gameView.getSize().y;
		float nextZoom= currentZoom*zoom;
		if(!LOCK_ZOOM||(nextZoom>=MIN_ZOOM&&nextZoom<=MAX_ZOOM))
			gameView.zoom(1/zoom);
		/*else if(currentZoom<1)
		{
			gameView.zoom(0.9f);
		}
		else if(currentZoom>3)
		{
			gameView.zoom(1.1f);
		}*/
	}
	public void changeActivePlayer(Player p)
	{
		if(activePlayer!=p)
		{
			activePlayer.clearAllSelected();
			activePlayer=p;
			gui.playerList.update();
			//if(SHOW_VISION_MASK)Main.worldMap.resetVision();
			if(SHOW_VISION_MASK)Main.worldMap.setVisionMap(p);
		}
	}
	public void runCommand(String s)
	{
		gui.console.runCommand(s);
	}
	public void registerCommands()
	{
		Commands.registerCommand("zoom_in",new CommandInterface(){
			@Override
			public void run(String[] args) {
				float zoomAmount=ZOOM_VALUE;
				if(args.length>0)
					try{zoomAmount=Float.parseFloat(args[0]);}catch(Exception e){}
				zoom(zoomAmount);
		}});
		Commands.registerCommand("zoom_out",new CommandInterface(){
			@Override
			public void run(String[] args) {
				float zoomAmount=1/ZOOM_VALUE;
				if(args.length>0)
					try{zoomAmount=1/(Float.parseFloat(args[0]));}catch(Exception e){}
				zoom(zoomAmount);
		}});
		Commands.registerCommand("toggle_edit_map",new CommandInterface(){
			@Override
			public void run(String[] args) {
				EDITING_MAP=!EDITING_MAP;
				if(EDITING_MAP)
					gui.mapEditorGui.enable();
				else
				{
					gui.mapEditorGui.disable();
					Main.worldMap.refreshImage();
				}
		}});
		Commands.registerCommand("toggle_show_flow",new CommandInterface(){
			@Override
			public void run(String[] args) {
				SHOW_FLOW=!SHOW_FLOW;
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
						runCommand("addUnit "+args[0]+" "+(int)gui.cursor.getGamePosition().x+" "+(int)gui.cursor.getGamePosition().y );
					}
					else if(args.length==2)
					{
						int repititions = Integer.parseInt(args[1]);
						for(int i=0;i<repititions;i++)
							gui.console.runCommand("addUnit "+args[0]);
					}
					else if(args.length==3)
					{
						String type=args[0];
						int x=Integer.parseInt(args[1]);
						int y=Integer.parseInt(args[2]);
						Entity e = UnitFactory.buildEntity(type, x, y, activePlayer);
						activePlayer.addUnit(e);
					}
				}catch(Exception e){e.printStackTrace();}
		}});
		Commands.registerCommand("spawnUnit",new CommandInterface(){
			@Override
			public void run(String[] args) {
				try{
					if(args.length==2)
					{
						String buildingID=args[0];
						String unitType=args[1];
						Building b=Building.allBuildings.get(buildingID);
						Vector2f spawnLoc = b.getSpawnLocation(); 
						Entity e=UnitFactory.buildEntity(unitType,(int)spawnLoc.x,(int)spawnLoc.y,b.player);
						if(e!=null)
							b.player.addUnit(e);
						if(b.rallyOrder!=null)b.rallyOrder.issue(e);						
					}
				}catch(Exception e){e.printStackTrace();}
		}});
		Commands.registerCommand("placeBuilding",new CommandInterface(){
			@Override
			public void run(String[] args) {
				try{
					String type = "barracks";
					if(args.length==1)
					{
						type=args[0];
					}
					gui.cursor.attachBuilding(type);
				}catch(Exception e){e.printStackTrace();}				
		}});
		Commands.registerCommand("addBuilding",new CommandInterface(){
			@Override
			public void run(String[] args) {
				try{
					if(args.length==1)
					{
						MapCell c = Main.worldMap.getCellAtPos(mouse.clickLoc);
						runCommand("addBuilding "+args[0]+" "+c.x+" "+c.y);
					}
					else if(args.length>=3)
					{
						String type=args[0];
						int x=Integer.parseInt(args[1]);
						int y=Integer.parseInt(args[2]);
						Building b=BuildingFactory.buildBuilding(type, activePlayer,x,y);
						b.completeConstruction();
					}
				}catch(Exception e){}
		}});
		Commands.registerCommand("addResource",new CommandInterface(){
			@Override
			public void run(String[] args) {
				try{
					if(args.length==1)
					{
						MapCell c = Main.worldMap.getCellAtPos(mouse.clickLoc);
						runCommand("addResource "+args[0]+" "+c.x+" "+c.y);
					}
					else if(args.length>=3)
					{
						String type=args[0];
						int x=Integer.parseInt(args[1]);
						int y=Integer.parseInt(args[2]);
						/*Building b=BuildingFactory.buildBuilding(type, activePlayer,x,y);
						b.completeConstruction();*/
						if(Main.worldMap.getCell(x, y).isTraversable())
							new Resource(type,100,x,y,activePlayer);
					}
				}catch(Exception e){}
		}});
		Commands.registerCommand("addFoundation",new CommandInterface(){
			@Override
			public void run(String[] args) {
				try{
					if(args.length==1)
					{
						MapCell c = Main.worldMap.getCellAtPos(mouse.clickLoc);
						runCommand("addFoundation "+args[0]+" "+c.x+" "+c.y);
					}
					else if(args.length>=3)
					{
						String type=args[0];
						int x=Integer.parseInt(args[1]);
						int y=Integer.parseInt(args[2]);
						BuildingFactory.buildBuilding(type, activePlayer,x,y);
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
				if(EDITING_MAP)
					Main.worldMap.setTerrainAtPosfour(mouse.clickLoc, gui.mapEditorGui.terrainType);
		}});
		Commands.registerCommand("saveMap",new CommandInterface(){
			@Override
			public void run(String[] args) {
				if(EDITING_MAP)
					try {
						Main.worldMap.saveToFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
		}});
		Commands.registerCommand("kill",new CommandInterface(){
			@Override
			public void run(String[] args) {
				while(!activePlayer.selectedUnits.isEmpty())
				{
					activePlayer.selectedUnits.get(0).kill();
				}
				while(!activePlayer.selectedBuildings.isEmpty())
				{
					activePlayer.selectedBuildings.get(0).destroy();
				}
		}});
		Commands.registerCommand("giveResource",new CommandInterface(){
			@Override
			public void run(String[] args) {
				try{
					if(args.length==3)
					{
						Player p=Main.players.get(Integer.parseInt(args[0]));
						p.collectResource(args[1], Integer.parseInt(args[2]));
					}
				}catch(Exception e){}
		}});
	}
}
