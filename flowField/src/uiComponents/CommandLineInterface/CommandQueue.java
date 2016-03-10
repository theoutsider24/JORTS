package uiComponents.CommandLineInterface;

import java.util.ArrayList;

public class CommandQueue {

	static ArrayList<CommandInterface> queue = new ArrayList<CommandInterface>();
	
	static ArrayList<CommandInterface> repeatedTasks = new ArrayList<CommandInterface>();
	static ArrayList<Integer> repetitions = new ArrayList<Integer>();
	
	public static void tick()
	{
		/*for(int i=0; i<repeatedTasks.size();i++)
		{
			queue.add(repeatedTasks.get(i));
			
			repetitions.set(i , repetitions.get(i)-1);  
			
			if(repetitions.get(i)<=0)
			{
				repetitions.remove(i);
				repeatedTasks.remove(i);
			}
		}
		
		for(CommandInterface c:queue)
		{
			c.run();
		}
		queue.clear();*/
	}
	public static void addTask(CommandInterface command, String[] args)
	{
		command.run(args);
		//queue.add(command);
	}
	/*public static void addRepeatedTask(CommandInterface command, int repetitions)
	{
		if(repetitions>0)
		{
			CommandQueue.repeatedTasks.add(command);
			CommandQueue.repetitions.add(repetitions);
		}
	}*/
	public static void clearRepeatedTasks()
	{
		repetitions.clear();
		repeatedTasks.clear();
	}
}
