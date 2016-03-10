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
import common.JsonReader;
import gameElements.map.Map;
import gameElements.map.MapCell;
import gameElements.units.Entity;
import gameElements.units.UnitFactory;

import static common.Constants.*;

import test.Logger;


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
		windows=new ArrayList<GameWindow>();
		worldMap = new Map(); 		
		players=new ArrayList<Player>();
		
		players=new ArrayList<Player>();
		players.add(new Player());
		players.add(new Player());
		players.add(new Player());
		
		
		JsonReader.readGameDefinition(gameDefinition);
		
		new GameWindow(players.get(0));
		//new GameWindow(players.get(1));
		//new GameWindow(players.get(1));
		/*mouse = new MouseManager();
		keyboard=new KeyboardManager();*/
		
		
		
		
		//initWindow();
		//gui = new GUI(uiView);
		//window1=new GameWindow();
		//window2=new GameWindow();
		//windows.add(window1);
		
			//new GameWindow(players.get(1));
			//windows.get(1).setPosition(new Vector2i(1920,0));
		//windows.add(new GameWindow());
		//windows.add(new GameWindow());
		
		int startingAreaSize = 400;
		for(int i=0;i<STARTING_UNIT_COUNT;i++)			
			players.get(0).addUnit(UnitFactory.buildEntity("infantry",(int) (players.get(0).startPosition.x+(int)(Math.random()*startingAreaSize)),(int) (players.get(0).startPosition.y+(int)(Math.random()*startingAreaSize)),players.get(0)));
		
		//zoom(.25f);
		for(GameWindow w:windows)
			w.gui.playerList.update();
		//registerCommands();
		clock=new Clock();
		while(windowOpen())
		{
			gameLoop();
		}
	}
	public boolean windowOpen()
	{
		for(GameWindow w:windows)
			if(w.isOpen())
				return true;
		return false;
	}
	public void gameLoop() throws IOException
	{
		deltaT=clock.getElapsedTime().asMilliseconds();
		if(!PAUSED)		
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
