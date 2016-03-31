package JORTS.gameElements.map;

import static JORTS.common.Constants.CELL_SIZE;
import static JORTS.common.Constants.GRID_SIZE;
import static JORTS.common.Constants.MAP_DIRECTORY;
import static JORTS.common.Constants.RESOLUTION_X;
import static JORTS.common.Constants.RESOLUTION_Y;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Scanner;
import java.util.Stack;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.Image;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import JORTS.common.CommonFunctions;
import JORTS.core.Player;
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
abstract class UpdatableImage extends RectangleShape
{
	Image image;
	Texture texture;
	public UpdatableImage(Vector2f shapeSize,Vector2f imageSize)
	{
		super(shapeSize);
		image = new Image();
		image.create((int)imageSize.x,(int)imageSize.y);
		texture = new Texture();
		//shape = new RectangleShape(new Vector2f(GRID_SIZE*CELL_SIZE,GRID_SIZE*CELL_SIZE));
	}
	public abstract void updateImage();
	protected void updateTexture()
	{
		try {texture.loadFromImage(image);} 
		catch (TextureCreationException e) {e.printStackTrace();}
		
		setTexture(texture);
	}
}
public class Map extends Observable implements Drawable{
	public MapCell[][] cells;
/*	Image image;
	Texture texture;
	RectangleShape shape;*/
	//UpdatableImage mapImage;
	public boolean suspendUpdate=false;
	public VisionMap visionMap;
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
	}
	public void setVisionMap(Player p)
	{
		visionMap=p.visionMap;
	}
	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) {		
		Vector2f topLeft=arg0.mapPixelToCoords(new Vector2i(0,0));
		topLeft = Vector2f.sub(topLeft, new Vector2f(CELL_SIZE,CELL_SIZE));
		Vector2f bottomRight=arg0.mapPixelToCoords(new Vector2i(RESOLUTION_X,RESOLUTION_Y));
		for(int i=0;i<GRID_SIZE;i++)
			for(int j=0;j<GRID_SIZE;j++)
			{
				if(CommonFunctions.isOnScreen(arg0,cells[i][j]))
					arg0.draw(cells[i][j]);
			}
		//arg0.draw(visionMask);
	}
	public void refreshImage()
	{
		if(!suspendUpdate)notifyAllObservers();
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
	public void closeCell(int x,int y)
	{
		if(cellExists(x,y)&&getCell(x,y).isTraversable())
		{
			getCell(x,y).close();
		}
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
	public void setTerrainAtPosfour(Vector2f pos,String terrainType)
	{
		if(TerrainFactory.prototypes.containsKey(terrainType))
		{
			System.out.println(terrainType);
			int x1=(int) (pos.x/CELL_SIZE);
			int y1= (int) (pos.y/CELL_SIZE);
					
			int x2=GRID_SIZE-1-x1;
			int y2= GRID_SIZE-1-y1;
			if(cellPosExists(pos.x,pos.y))
			{
				/*cells[x1][y1]=TerrainFactory.buildCell(cells[x1][x2], terrainType);
				cells[y1][x1]=TerrainFactory.buildCell(cells[x1][x2], terrainType);
				cells[x2][y2]=TerrainFactory.buildCell(cells[x1][x2], terrainType);
				cells[y2][x2]=TerrainFactory.buildCell(cells[x1][x2], terrainType);*/
				TerrainFactory.buildCell(cells[x1][y1], terrainType);
				TerrainFactory.buildCell(cells[y1][x1], terrainType);
				TerrainFactory.buildCell(cells[x2][y2], terrainType);
				TerrainFactory.buildCell(cells[y2][x2], terrainType);
			}
			//refreshImage();
		}
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
		return (x>=0&&y>=0&&x<GRID_SIZE*CELL_SIZE&&y<GRID_SIZE*CELL_SIZE);		
	}
	public boolean cellPosExists(Vector2f v)
	{
		return (v.x>=0&&v.y>=0&&v.x<GRID_SIZE*CELL_SIZE&&v.y<GRID_SIZE*CELL_SIZE);		
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
		JFileChooser chooser = new JFileChooser(MAP_DIRECTORY);
		
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
		System.out.println(fileName);
    	if(!fileName.endsWith(".mp"))
    		fileName+=".mp";
		BufferedWriter outputWriter = null;
		outputWriter = new BufferedWriter(new FileWriter(MAP_DIRECTORY+"//"+fileName));
		for (int i = 0; i < GRID_SIZE; i++) 
		{
			for (int j = 0; j < GRID_SIZE; j++) 
			{
				outputWriter.write(cells[i][j].terrain.terrainType);
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
				catch(NoSuchElementException ex){}
				Scanner lineScanner = new Scanner(line);
				
				for (int j = 0; j < GRID_SIZE; j++) 
				{
					String x=TerrainFactory.defaultTerrain;
					try{x = lineScanner.next("[^\\s]+");}
					catch(NoSuchElementException ex){}
					cells[i][j]=TerrainFactory.buildCell(i, j, ""+x);
				}
			}
			refreshImage();	
		}
		catch(FileNotFoundException ex){}
	}
	public void loadFromFile() throws FileNotFoundException
	{	
		JFileChooser chooser = new JFileChooser(MAP_DIRECTORY);
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
	public void highlightCell(int x,int y, Color c)
	{
		try
		{
			cells[x][y].highlight(c);
			//mapImage.updateImage();
		}catch(Exception e){}
	}
	public void unhighlightAll()
	{
		boolean updateRequired=false;
		for(int i=0;i<GRID_SIZE;i++)
			for(int j=0;j<GRID_SIZE;j++)
			{
				if(cells[i][j].highlighted)
				{
					cells[i][j].unhighlight();
					updateRequired=true;
				}
			}
		//if(updateRequired)mapImage.updateImage();
	}
	
}
