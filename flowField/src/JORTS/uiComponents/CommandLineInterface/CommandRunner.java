package JORTS.uiComponents.CommandLineInterface;

import JORTS.core.GameWindow;

public class CommandRunner implements Runnable{
	GameWindow window;
	String command;
	public CommandRunner(GameWindow w,String c)
	{
		window=w;
		command=c;
	}
	@Override
	public void run() {
		window.gui.console.runCommand(command);
	}
	

}
