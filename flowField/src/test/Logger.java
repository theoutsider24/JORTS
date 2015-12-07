package test;

import static common.Constants.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import units.Entity;
import FYP.Main;

public class Logger {
	public static void logUnitPositions() throws IOException
	{   
    	String fileName=LOG_DIRECTORY+"\\unitPos.log";
		BufferedWriter outputWriter = null;
		outputWriter = new BufferedWriter(new FileWriter(fileName));
		
		for (Entity e:Main.activePlayer.getUnits()) 
		{
			outputWriter.write(e.toString());
			outputWriter.newLine();
		}
		outputWriter.flush();  
		outputWriter.close();
	    
	}
}
