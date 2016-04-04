package JORTS.core;

import static JORTS.common.Constants.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Observable;

import org.jsfml.graphics.Font;
import org.jsfml.graphics.Text;
import org.jsfml.system.Clock;

import JORTS.common.JsonReader;
import JORTS.communication.EventManager;
import JORTS.communication.Server;
import JORTS.gameElements.map.Map;
import JORTS.gameElements.units.UnitFactory;


public class Main extends Observable{
	public static Font font;
	public static long milliseconds;
	public Clock clock;
	
	public static int cellsToOpen = 10;
	public static ArrayList<Player> players;
	public static Map worldMap;
	public static ArrayList<GameWindow> windows;
 	
	public static Main game;
	public static long deltaT;
	static String gameDefinition="";
	public static String initScript="";
	public Server server;
	public static void main(String[] args) throws IOException
	{
		if(args.length>0)
		{
			gameDefinition = args[0];
		}
		System.out.println(args[0]);
		

		game = new Main();
		game.init();
	}
	public void init() throws IOException
	{
		loadFont(FONT);		
		worldMap = new Map(); 	
		worldMap.suspendUpdate=true;
		windows=new ArrayList<GameWindow>();
		players=new ArrayList<Player>();
		JsonReader.readGameDefinition(gameDefinition);
		
		new GameWindow(players.get(0));
		//new GameWindow(players.get(1));
		//new GameWindow(players.get(1));
		//new GameWindow(players.get(1));
		/*mouse = new MouseManager();
		keyboard=new KeyboardManager();*/
		
			//new GameWindow(players.get(1));
			//windows.get(1).setPosition(new Vector2i(1920,0));
		//windows.add(new GameWindow());
		//windows.add(new GameWindow());
		
		clock=new Clock();		
		windows.get(0).runCommand(initScript);
		
		server = new Server();
		server.start();
		worldMap.suspendUpdate=false;
		worldMap.refreshImage();
		
		while(windowOpen())
		{
			gameLoop();
		}
		server.close();
	}
	public boolean windowOpen()
	{
		for(GameWindow w:windows)
			if(w.isOpen())
				return true;
		return false;
	}
	public Player getPlayer(String s)
    {
  	  for(Player p:players)
	  {
		  if(p.name.equals(s))
		  {
			 return p;
		  }
	  }
	  return null;
    }
	public void gameLoop() throws IOException
	{
		deltaT=0;	
		if(!PAUSED)		
		{
			deltaT=clock.getElapsedTime().asMilliseconds();
		}
		milliseconds+=deltaT;
		clock.restart();
				
		setChanged();
		notifyObservers();
		worldMap.unhighlightAll();
		for(GameWindow w:windows)
			w.tick();
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
	public static GameWindow getPlayerWindow(Player p)
	{
		GameWindow wind=null;
		for(GameWindow w:windows)
		{			
			if(w.activePlayer==p)
			{
				if(wind==null)
					wind= w;
				if(w.hasFocus)
					wind=w;
			}			
		}
		return wind;
	}
	public static GameWindow getActiveWindow()
	{		
		for(GameWindow w:windows)
		{
			if(w.hasFocus)
				return w;
		}
		return null;
	}
	public static void takeFocus(GameWindow wind)
	{
		for(GameWindow w:windows)
		{
			w.hasFocus=false;
			w.setTitle(WINDOW_TITLE);
		}
		wind.hasFocus=true;
	}
}
