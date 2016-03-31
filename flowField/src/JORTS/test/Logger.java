package JORTS.test;

import static JORTS.common.Constants.LOG_DIRECTORY;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
	public static void logUnitPositions() throws IOException
	{   
    	String fileName=LOG_DIRECTORY+"\\unitPos.log";
		BufferedWriter outputWriter = null;
		outputWriter = new BufferedWriter(new FileWriter(fileName));
		
		/*for (Entity e:Main.window1.activePlayer.getUnits()) 
		{
			outputWriter.write(e.toString());
			outputWriter.newLine();
		}*/
		outputWriter.flush();  
		outputWriter.close();
	    
	}
}
