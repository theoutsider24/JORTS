package uiComponents.CommandLineInterface;

import java.util.ArrayList;

import org.jsfml.window.Keyboard;
import org.jsfml.window.Keyboard.Key;

import FYP.GameWindow;

public class ModifiedCommandRunner extends CommandRunner{
	ArrayList<Key> requiredKeys;
	public ModifiedCommandRunner(GameWindow w, String c,Key... keys) {
		super(w, c);
		requiredKeys=new ArrayList<Keyboard.Key>();
		for(Key k:keys)
		{
			requiredKeys.add(k);
		}
	}
	public boolean modifiersPressed()
	{
		boolean valid=true;
		if(requiredKeys.size()==0)
			return true;
		for(Key k:requiredKeys)
		{
			System.out.println(k.toString());
			if(!Keyboard.isKeyPressed(k))
			{
				return false;
			}
		}
		return valid;
	}
	@Override
	public void run() {
		if(modifiersPressed())
		{
			super.run();
		}
	}
}
