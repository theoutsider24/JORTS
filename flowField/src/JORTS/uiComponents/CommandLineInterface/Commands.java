package JORTS.uiComponents.CommandLineInterface;

import java.util.HashMap;

public class Commands {
	static HashMap<String, CommandInterface> commandPairs = new HashMap<String, CommandInterface>();
	public static boolean submitCommand(String command, String[] args)
	{
		command=command.toLowerCase();
				
		if(commandPairs.containsKey(command))
		{
			CommandQueue.addTask(commandPairs.get(command),args);
			return true;
		}
		else
			return false;
	}
	
	
	
	public static boolean submitCommand(String command)
	{
		return submitCommand(command, new String[]{});
	}
	public static void registerCommand(String commandWord, CommandInterface commandMethod)
	{
		commandWord=commandWord.toLowerCase();
		if(!commandPairs.containsKey(commandWord))
			commandPairs.put(commandWord, commandMethod);
	}
}
