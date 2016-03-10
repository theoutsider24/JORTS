package uiComponents.uiManagers;

import static common.Constants.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsfml.window.Keyboard;
import static org.jsfml.window.Keyboard.Key.*;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;

import FYP.GameWindow;
import FYP.Main;
import uiComponents.CommandLineInterface.CommandRunner;

public class KeyboardManager {
	public static ArrayList<Keyboard.Key> numKeys;
	GameWindow window;
	public HashMap<Keyboard.Key,Runnable> keyMappings;
	public static ArrayList<String[]> loadedMappings=new ArrayList<String[]>();;
	public KeyboardManager(GameWindow window)
	{
		this.window=window;
		keyMappings=new HashMap<Keyboard.Key,Runnable>();
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
		mapKeys();
	}
	public static void loadMapping(String key,String cmd)
	{
		loadedMappings.add(new String[]{key,cmd});
	}
	public void addLoadedMappings()
	{
		for(String[] s:loadedMappings)
		{
			mapKeyCommand(s[0],s[1]);
		}
	}
	public void mapKeyCommand(Keyboard.Key key,String cmd)
	{
		keyMappings.put(key, new CommandRunner(window,cmd));
	}
	public void mapKeyCommand(String key,String cmd)
	{
		mapKeyCommand(Keyboard.Key.valueOf(key),cmd);
	}
	private void mapKeys()
	{
		addLoadedMappings();
		//mapKeyCommand(ESCAPE,"exit");
		//mapKeyCommand(UP,"move_camera 0 -30");
		//mapKeyCommand(DOWN,"move_camera 0 30");
		//mapKeyCommand(LEFT,"move_camera -30 0");
		//mapKeyCommand(RIGHT,"move_camera 30 0");
		
		/*mapKeyCommand(ADD,"zoom_in 2");
		mapKeyCommand(F1,"setPlayer 0");
		mapKeyCommand(F2,"setPlayer 1");
		mapKeyCommand(F3,"setPlayer 2");
		mapKeyCommand(SUBTRACT,"zoom_out 2");	*/	
		
		keyMappings.put(Key.L, new Runnable(){@Override public void run(){try {
			Main.worldMap.loadFromFile();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}});		
		keyMappings.put(Key.RETURN, new Runnable(){@Override public void run(){window.gui.console.open();}});
		keyMappings.put(Key.Z, new Runnable(){@Override public void run(){if(Keyboard.isKeyPressed(Keyboard.Key.LCONTROL))
			Main.worldMap.undo();}});
		keyMappings.put(Key.N, new Runnable(){@Override public void run(){if(Keyboard.isKeyPressed(Keyboard.Key.LCONTROL))
			Main.worldMap.openAll();}});
		keyMappings.put(Key.Y, new Runnable(){@Override public void run(){if(Keyboard.isKeyPressed(Keyboard.Key.LCONTROL))
			Main.worldMap.redo();}});
		keyMappings.put(Key.PAUSE, new CommandRunner(window,"pause"));
	}
	public void processKeypress(Event event) throws IOException
	{
		if(window.gui.console.isOpen)
		{
			
			if(event.type == Event.Type.TEXT_ENTERED)
			{
				char character = event.asTextEvent().character;
				if(Character.isDefined(character))
				{	
					if((int)character==13||(int)character==8)
						;
					else
						window.gui.console.addCharacter(character+"");
				}
			}
			else if (event.type == Event.Type.KEY_PRESSED)
			{
				if(event.asKeyEvent().key==Keyboard.Key.BACKSPACE)
					window.gui.console.backspace();
				else if(event.asKeyEvent().key==Keyboard.Key.ESCAPE)
					window.gui.console.close();
				else if(event.asKeyEvent().key==Keyboard.Key.RETURN)
					window.gui.console.submitText();		
				else if(event.asKeyEvent().key==Keyboard.Key.DOWN)
		    	{
					window.gui.console.loadPreviousEntry();
		    	}
				else if(event.asKeyEvent().key==Keyboard.Key.UP)
		    	{
					window.gui.console.loadNextEntry();
		    	}
			}
		}
		else if (event.type == Event.Type.KEY_PRESSED)
		{
			KeyEvent e = event.asKeyEvent();
			if(keyMappings.containsKey(e.key))
				keyMappings.get(e.key).run();
			/*switch(e.key)
	    	{
	    		case ESCAPE: window.close();
	    			break;
	    		case UP: window.moveCamera(0,-30); 
	    			break;
	    		case DOWN: window.moveCamera(0,30); 
	    			break;
	    		case LEFT: window.moveCamera(-30,0); 
	    			break;
	    		case RIGHT: window.moveCamera(30,0);
					break;
	    		case ADD: window.zoom(2); 
	    			break;
	    		case F1: window.changeActivePlayer(Main.players.get(0));
	    			break;
	    		case F2: window.changeActivePlayer(Main.players.get(1));
	    			break;
	    		case F3: window.changeActivePlayer(Main.players.get(2));
	    			break;
	    		case SUBTRACT: window.zoom(.5f); 
	    			break;
	    		case S: if(!Keyboard.isKeyPressed(Keyboard.Key.LCONTROL))
	    					Main.worldMap.saveToFile();//TODO breaks game
						else
							Main.worldMap.saveFile(DEFAUL_MAP);	
	    				; 
					break;
	    		case L: Main.worldMap.loadFromFile(); 
	    			break;
	    		case RETURN: window.gui.console.open();
				break;
	    		case Z: if(Keyboard.isKeyPressed(Keyboard.Key.LCONTROL))
	    					Main.worldMap.undo();
	    			break;
	    		case N: if(Keyboard.isKeyPressed(Keyboard.Key.LCONTROL))
	    					Main.worldMap.openAll();
					break;
	    		case Y: if(Keyboard.isKeyPressed(Keyboard.Key.LCONTROL))
	    					Main.worldMap.redo();
				break;
	    		case PAUSE: PAUSED=!PAUSED;
	    			break;
	    		default: 
	    			if(numKeys.contains(e.key))
					{
	    				if(Keyboard.isKeyPressed(Keyboard.Key.LCONTROL))
	    					window.activePlayer.assignControlGroup(numKeys.indexOf(e.key));
	    				else
	    					window.activePlayer.selectControlGroup(numKeys.indexOf(e.key));
					}
					break;       
	    	}*/
    	}
	}
}
