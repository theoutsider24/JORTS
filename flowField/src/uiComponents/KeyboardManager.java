package uiComponents;

import static common.Constants.DEFAUL_MAP;

import java.io.IOException;
import java.util.ArrayList;

import org.jsfml.window.Keyboard;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;

import FYP.Main;

public class KeyboardManager {
	public static ArrayList<Keyboard.Key> numKeys;
	public KeyboardManager()
	{
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
	}
	public void processKeypress(Event event) throws IOException
	{
		if(Main.gui.console.isOpen)
		{
			
			if(event.type == Event.Type.TEXT_ENTERED)
			{
				char character = event.asTextEvent().character;
				if(Character.isDefined(character))
				{	
					if((int)character==13||(int)character==8)
						;
					else
						Main.gui.console.addCharacter(character+"");
				}
			}
			else if (event.type == Event.Type.KEY_PRESSED)
			{
				if(event.asKeyEvent().key==Keyboard.Key.BACKSPACE)
	    			Main.gui.console.backspace();
				else if(event.asKeyEvent().key==Keyboard.Key.ESCAPE)
	    			Main.gui.console.close();
				else if(event.asKeyEvent().key==Keyboard.Key.RETURN)
	    			Main.gui.console.submitText();		
				else if(event.asKeyEvent().key==Keyboard.Key.DOWN)
		    	{
					Main.gui.console.loadPreviousEntry();
		    	}
				else if(event.asKeyEvent().key==Keyboard.Key.UP)
		    	{
					Main.gui.console.loadNextEntry();
		    	}
			}
		}
		else if (event.type == Event.Type.KEY_PRESSED)
		{
			KeyEvent e = event.asKeyEvent();
			switch(e.key)
	    	{
	    		case ESCAPE: Main.window.close();
	    			break;
	    		case UP: Main.moveCamera(0,-30); 
	    			break;
	    		case DOWN: Main.moveCamera(0,30); 
	    			break;
	    		case LEFT: Main.moveCamera(-30,0); 
	    			break;
	    		case RIGHT: Main.moveCamera(30,0);
					break;
	    		case ADD: Main.zoom(2); 
	    			break;
	    		case F1: Main.activePlayer=Main.players.get(0); Main.gui.playerList.update();
	    			break;
	    		case F2: Main.activePlayer=Main.players.get(1); Main.gui.playerList.update();
	    			break;
	    		case F3: Main.activePlayer=Main.players.get(2); Main.gui.playerList.update();
	    			break;
	    		case SUBTRACT: Main.zoom(.5f); 
	    			break;
	    		/*case S: if(!Keyboard.isKeyPressed(Keyboard.Key.LCONTROL))
	    					Main.worldMap.saveToFile();//TODO breaks game
						else
							Main.worldMap.saveFile(DEFAUL_MAP);	
	    				; 
					break;*/
	    		case L: Main.worldMap.loadFromFile(); 
	    			break;
	    		case RETURN: Main.gui.console.open();
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
	    		default: 
	    			if(numKeys.contains(e.key))
					{
	    				if(Keyboard.isKeyPressed(Keyboard.Key.LCONTROL))
	    					Main.activePlayer.assignControlGroup(numKeys.indexOf(e.key));
	    				else
	    					Main.activePlayer.selectControlGroup(numKeys.indexOf(e.key));
					}
					break;       
	    	}
    	}
	}
}
