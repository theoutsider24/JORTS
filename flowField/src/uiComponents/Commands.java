package uiComponents;

import java.util.HashMap;

public class Commands {
	static HashMap<String, CommandInterface> commandPairs = new HashMap<String, CommandInterface>();
	public static boolean submitCommand(String command, String[] args)
	{
		command=command.toLowerCase();
		int repetitions =0;
		if(args.length>0)
		try
		{
			repetitions=Integer.parseInt(args[0]);
		}
		catch (NumberFormatException ex)
		{}
		
		if(commandPairs.containsKey(command))
		{
			System.out.println(command);
			if(repetitions>0)
				CommandQueue.addRepeatedTask(commandPairs.get(command), repetitions);
			else
				CommandQueue.addTask(commandPairs.get(command));
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
