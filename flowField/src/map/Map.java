package map;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.Stack;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.Image;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.system.Vector2f;

import FYP.Main;
import FYP.flowField.FlowCell;
import common.CommonFunctions;

import static common.Constants.*;
class EditCommand
{
	Vector2f pos;
	String command;
	public EditCommand(Vector2f pos,String command)
	{
		this.pos=pos;
		this.command=command;
	}
}
public class Map extends Observable implements Drawable{
	public MapCell[][] cells;
	Image image;
	Texture texture;
	RectangleShape shape;
	Stack<EditCommand> undoStack;
	Stack<EditCommand> redoStack;
	public Map()
	{
		undoStack=new Stack<EditCommand>();
		redoStack=new Stack<EditCommand>();
		cells=new MapCell[GRID_SIZE][GRID_SIZE];
		for(int i=0;i<GRID_SIZE;i++)
			for(int j=0;j<GRID_SIZE;j++)
				cells[i][j] = new MapCell(i,j);
		image = new Image();
		image.create(GRID_SIZE,GRID_SIZE);
		texture = new Texture();
		shape = new RectangleShape(new Vector2f(GRID_SIZE*CELL_SIZE,GRID_SIZE*CELL_SIZE));
		refreshImage();
	}
	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) {		
		arg0.draw(shape);
	}
	public void refreshImage()
	{
		for(int i=0;i<GRID_SIZE;i++)
			for(int j=0;j<GRID_SIZE;j++)
			{
				image.setPixel(i, j, cells[i][j].getDrawColor());
			}
		
		try {texture.loadFromImage(image);} 
		catch (TextureCreationException e) {e.printStackTrace();}
		
		shape.setTexture(texture);
		notifyAllObservers();
	}
	
	public boolean isCellTraversable(int i,int j)
	{
		if(cellExists(i,j))
			return cells[i][j].isTraversable();
		else
			return false;
	}
	public void openCellAtPos(Vector2f pos,boolean log)
	{
		if(log)pushUndo(new EditCommand(pos, "open"));
		if(cellPosExists(pos.x,pos.y))
		{
			getCellAtPos(pos).open();
		}
		refreshImage();
	}
	public void openCellsAtPosFour(Vector2f pos,boolean log)
	{		
		if(log)pushUndo(new EditCommand(pos, "open4"));
		int x1=(int) (pos.x/CELL_SIZE);
		int y1= (int) (pos.y/CELL_SIZE);
		
		int x2=GRID_SIZE-1-x1;
		int y2= GRID_SIZE-1-y1;
		if(cellPosExists(pos.x,pos.y))
		{
			getCell(x1,y1).open();
			getCell(y1,x1).open();
			//getCell(x1,y2).open();
			//getCell(x2,y1).open();
			getCell(y2,x2).open();
			getCell(x2,y2).open();
		}
		refreshImage();
	}
	public void closeCellsAtPosFour(Vector2f pos,boolean log)
	{
		if(log)pushUndo(new EditCommand(pos, "close4"));
		int x1=(int) (pos.x/CELL_SIZE);
		int y1= (int) (pos.y/CELL_SIZE);
				
		int x2=GRID_SIZE-1-x1;
		int y2= GRID_SIZE-1-y1;
		if(cellPosExists(pos.x,pos.y))
		{
			getCell(x1,y1).close();
			getCell(y1,x1).close();
			//getCell(x1,y2).open();
			//getCell(x2,y1).open();
			getCell(y2,x2).close();
			getCell(x2,y2).close();
		}
		refreshImage();
	}
	public void closeCellAtPos(Vector2f pos,boolean log)
	{
		if(log)pushUndo(new EditCommand(pos, "close"));
		if(cellPosExists(pos.x,pos.y))
		{
			getCellAtPos(pos).close();
		}
		refreshImage();
	}
	public boolean cellExists(int x,int y)
	{
		return (x>=0&&y>=0&&x<GRID_SIZE&&y<GRID_SIZE);
	}
	public boolean cellPosExists(float x,float y)
	{
		return (x>0&&y>0&&x<GRID_SIZE*CELL_SIZE&&y<GRID_SIZE*CELL_SIZE);		
	}
	public boolean cellPosExists(Vector2f v)
	{
		return (v.x>0&&v.y>0&&v.x<GRID_SIZE*CELL_SIZE&&v.y<GRID_SIZE*CELL_SIZE);		
	}
	
	public MapCell getCellAtPos(Vector2f pos) throws IndexOutOfBoundsException
	{
		if(cellPosExists(pos))
			return cells[(int) (pos.x/CELL_SIZE)][(int) (pos.y/CELL_SIZE)];
		else
			return new MapCell(0,0);
	}
	public MapCell getCell(int x,int y) throws IndexOutOfBoundsException
	{
		if(cellExists(x,y))
			return cells[x][y];
		else
			return new MapCell(0,0);		
	}
	public void saveToFile() throws IOException
	{
		JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "MapFiles", "mp");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showSaveDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) 
	    {	    
	    	saveFile(chooser.getSelectedFile().getName());
	    }
	}
	public void saveFile(String fileName) throws IOException
	{
		
    	if(!fileName.endsWith(".mp"))
    		fileName+=".mp";
		BufferedWriter outputWriter = null;
		outputWriter = new BufferedWriter(new FileWriter(fileName));
		for (int i = 0; i < GRID_SIZE; i++) 
		{
			for (int j = 0; j < GRID_SIZE; j++) 
			{
				outputWriter.write(""+cells[i][j].getCost());
				if(j!=GRID_SIZE-1)
					outputWriter.write(" ");
			}
			outputWriter.newLine();
		}
		outputWriter.flush();  
		outputWriter.close();
	}
	public void loadFile(File file)
	{
		try{
			Scanner s = new Scanner(file);
			for (int i = 0; i < GRID_SIZE; i++) 
			{
				String line="";			
				try{line = s.nextLine();}
				catch(NoSuchElementException ex){break;}
				Scanner lineScanner = new Scanner(line);
				
				for (int j = 0; j < GRID_SIZE; j++) 
				{
					int x=0;
					try{x = lineScanner.nextInt();}
					catch(NoSuchElementException ex){break;}
					cells[i][j].setCost(x);
				}
			}
			refreshImage();	
		}
		catch(FileNotFoundException ex){}
	}
	public void loadFromFile() throws FileNotFoundException
	{	
		JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "MapFiles", "mp");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(null);
	    
	    if(returnVal == JFileChooser.APPROVE_OPTION) 
	    {
			loadFile(chooser.getSelectedFile());
	    }
	}
	public void closeLine(int x1,int y1,int x2,int y2)
	{
		ArrayList<int[]> points = CommonFunctions.getBresenhamLine(x1, y1, x2, y2);
		for(int[] p:points)		
		    getCell(p[0], p[1]).close();
		refreshImage();
	}
	public void undo()
	{
		if(!undoStack.isEmpty())
		{
			EditCommand com = undoStack.pop();
			if(com.command.equals("close"))
				openCellAtPos(com.pos,false);
			else if(com.command.equals("open"))
				closeCellAtPos(com.pos,false);
			else if(com.command.equals("close4"))
				openCellsAtPosFour(com.pos,false);
			else if(com.command.equals("open4"))
				closeCellsAtPosFour(com.pos,false);
			redoStack.push(com);
		}
	}
	public void redo()
	{
		if(!redoStack.isEmpty())
		{
			EditCommand com = redoStack.pop();
			if(com.command.equals("open"))
				openCellAtPos(com.pos,false);
			else if(com.command.equals("close"))
				closeCellAtPos(com.pos,false);
			else if(com.command.equals("open4"))
				openCellsAtPosFour(com.pos,false);
			else if(com.command.equals("close4"))
				closeCellsAtPosFour(com.pos,false);
			undoStack.push(com);
		}
	}
	public void pushUndo(EditCommand com)
	{
		undoStack.push(com);
		redoStack.clear();
	}
	public void notifyAllObservers()
	{
		setChanged();
		notifyObservers();
		clearChanged();
	}
	public void openAll()
	{
		for(int i=0;i<GRID_SIZE;i++)
			for(int j=0;j<GRID_SIZE;j++)
			{
				getCell(i,j).open();
			}
		refreshImage();	
	}
	
}
