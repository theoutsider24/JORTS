package FYP;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Properties;

import uiComponents.grids.*;
import uiComponents.*;
import uiComponents.buttons.*;
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
	
	public static int cellsToOpen = 10;
	//public static Player activePlayer,player2,player3,activePlayer;
	public static Player activePlayer;
	public static ArrayList<Player> players;
	public static Map worldMap;
	public static boolean editMapMode=false;
	public static boolean showFlow=false;
	public static View gameView;
	public static View uiView;
	
	
	public static boolean hasFocus=true;
	public static Clock clock;
	public static GUI gui;
	
	public static String hoverIntent="";
	public static MouseManager mouse;
	public static KeyboardManager keyboard;
 	
	public static Main game;
	public long deltaT;
	public long lastTimer;
	
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
		mouse = new MouseManager();
		keyboard=new KeyboardManager();
		
		worldMap = new Map(); 
		worldMap.loadFile(new File(DEFAUL_MAP));
		players=new ArrayList<Player>();
		players.add(new Player());
		players.add(new Player());
		players.add(new Player());
		activePlayer=players.get(0);
		
		initWindow();
		gui = new GUI(uiView);
		
		int startingAreaSize = 400;
		for(int i=0;i<STARTING_UNIT_COUNT;i++)			
			players.get(0).addUnit(new Infantry((int) (players.get(0).startPosition.x+(int)(Math.random()*startingAreaSize)),(int) (players.get(0).startPosition.y+(int)(Math.random()*startingAreaSize))));
		
		zoom(.25f);
		clock = new Clock();

		Main.gui.playerList.update();
		registerCommands();
		while(window.isOpen())
		{
			gameLoop();
		}
	}
	public void gameLoop() throws IOException
	{
		deltaT=gui.clock.clock.getElapsedTime().asMilliseconds()-lastTimer;
		lastTimer=gui.clock.clock.getElapsedTime().asMilliseconds();
		setChanged();
		notifyObservers();
		worldMap.unhighlightAll();
		if(hasFocus)mouse.limitMouse();
		   pollEvents();
		   
		  /* for(Player p:players)
			   p.tick();*/
		   window.clear(new Color(0,0,100));
		   window.setView(gameView);
		   if(SHOW_VISION_MASK)worldMap.refreshVisionMask();
		   window.draw(worldMap);
		   if(showFlow)
			   window.draw(activePlayer.currentField);
		   
		   for(Player p:players)
			   window.draw(p);
		   if(SHOW_VISION_MASK) window.draw(worldMap.visionMask);
		   window.draw(gui);
		   //updateFPSTimer();
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
	
	public static void pollEvents() throws IOException
	{
		mouse.setMouseLocs();
		mouse.determineHoverIntent();
		for(Event event : window.pollEvents()) 
		{
			if(event.type == Event.Type.CLOSED) 
	        {
	            window.close();
	        }
			if(event.type==Event.Type.GAINED_FOCUS)
				hasFocus=true;
			if(event.type==Event.Type.LOST_FOCUS)
				hasFocus=false;
				
			mouse.processEvent(event);
	        if(event.type == Event.Type.KEY_PRESSED||event.type == Event.Type.TEXT_ENTERED)
    		{
	        	keyboard.processKeypress(event);
    		}
	    }
	}
	public static void moveCamera(int x,int y)
	{
		gameView.setCenter(gameView.getCenter().x+x, gameView.getCenter().y+y);
	}
	public static void updateFPSTimer()
	{
		 window.setTitle(""+(int)(1000.0/clock.getElapsedTime().asMilliseconds()));
		   clock.restart();
	}
	public static void zoom(float zoom)
	{
		gameView.setSize(gameView.getSize().x / zoom, gameView.getSize().y / zoom);
	}
	public void registerCommands()
	{
		Commands.registerCommand("zoom_in",new CommandInterface(){
			@Override
			public void run() {
				Main.zoom(1.3f);
		}});
		Commands.registerCommand("zoom_out",new CommandInterface(){
			@Override
			public void run() {
				Main.zoom(1/1.3f);
		}});
	}
	public void changeActivePlayer(Player p)
	{
		activePlayer=p;
		Main.gui.playerList.update();
		worldMap.resetVision();
	}
}
