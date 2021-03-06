package JORTS.uiComponents.uiManagers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsfml.window.Keyboard;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;

import JORTS.core.GameWindow;
import JORTS.core.Main;
import JORTS.uiComponents.CommandLineInterface.CommandRunner;
import JORTS.uiComponents.CommandLineInterface.ModifiedCommandRunner;


public class KeyboardManager {
	GameWindow window;
	public HashMap<Keyboard.Key,Runnable> keyMappings;
	public HashMap<Keyboard.Key,Runnable> modifiedKeyMappings;
	public static ArrayList<String[]> loadedMappings=new ArrayList<String[]>();
	public KeyboardManager(GameWindow window)
	{
		this.window=window;
		keyMappings=new HashMap<Keyboard.Key,Runnable>();
		modifiedKeyMappings=new HashMap<Keyboard.Key,Runnable>();
		mapKeys();
	}
	public static void loadMapping(String key,String cmd,String mod)
	{
		loadedMappings.add(new String[]{key,cmd,mod});
	}
	public void addLoadedMappings()
	{
		for(String[] s:loadedMappings)
		{
			mapKeyCommand(s[0],s[1],s[2]);
		}
	}
	public void mapKeyCommand(Keyboard.Key key,String cmd,Key mod)
	{
		modifiedKeyMappings.put(key, new ModifiedCommandRunner(window,cmd,mod));		
	}
	public void mapKeyCommand(Keyboard.Key key,String cmd)
	{
		keyMappings.put(key, new CommandRunner(window,cmd));				
	}
	
	public void mapKeyCommand(String key,String cmd,String mod)
	{
		
		try{Key modKey = Keyboard.Key.valueOf(mod);
		mapKeyCommand(Keyboard.Key.valueOf(key),cmd,modKey);}
		catch(Exception ex){
			mapKeyCommand(Keyboard.Key.valueOf(key),cmd);
			}
	}
	private void mapKeys()
	{
		addLoadedMappings();
		
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
					if(!((int)character==13||(int)character==8))						
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
			boolean succesful=false;
			if(modifiedKeyMappings.containsKey(e.key))
			{
			//	System.out.println("test");
				ModifiedCommandRunner m = (ModifiedCommandRunner)modifiedKeyMappings.get(e.key);
				m.run();
				if(m.modifiersPressed())
					succesful=true;
			//	System.out.println(succesful);
			}
			if(!succesful&&keyMappings.containsKey(e.key))
			{
			//	System.out.println(keyMappings.get(e.key));
				keyMappings.get(e.key).run();
			}
    	}
	}
}
