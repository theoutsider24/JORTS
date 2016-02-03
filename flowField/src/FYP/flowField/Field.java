package FYP.flowField;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Observable;
import java.util.Observer;
import java.util.spi.LocaleServiceProvider;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.Image;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Text;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import FYP.Main;
import common.CommonFunctions;

import static common.Constants.*;
import map.Map;

public class Field  implements Drawable,Observer{
	//public static final int CELL_SIZE=20;
	//public static final int GRID_SIZE=100;
	//static final int GRID_SIZE = Main.resolution.x/CELL_SIZE;
	
	Image image;
	Texture texture;
	RectangleShape shape;
	
	ArrayList<FlowCell> openList;
	ArrayList<FlowCell> closedList;
	FlowCell[][] cells;
	

	FlowCell goal;
	ArrayList<FlowCell> goals = new ArrayList<FlowCell>();
	
	public Vector2f goalPosition;
	
	int initialCellsToOpen=0;
	public static final Field nullField=new Field(Main.worldMap);
	int cellsToOpen; 
	
	/*public static init(int CELL_SIZE,int GRID_SIZE)
	{
		this.CELL_SIZE = CELL_SIZE;
		this.GRID_SIZE = GRID_SIZE;
	}*/
	
	public Field(Map worldMap,ArrayList<InfluenceMap> influenceMaps)
	{		
		this(worldMap);
		for(InfluenceMap infMap:influenceMaps)
		{
			for(int i=0;i<GRID_SIZE;i++)
				for(int j=0;j<GRID_SIZE;j++)
					if(cells[i][j].cost!=0)
						cells[i][j].increaseCost(infMap.costs[i][j]);
		}
	}
	public Field(Map worldMap)
	{
		worldMap.addObserver(this);
		image = new Image();
		image.create(GRID_SIZE,GRID_SIZE);
		texture = new Texture();
		shape = new RectangleShape(new Vector2f(GRID_SIZE*CELL_SIZE,GRID_SIZE*CELL_SIZE));
		
		goalPosition=new Vector2f(-1,0);
		
		openList = new ArrayList<FlowCell>();
		closedList = new ArrayList<FlowCell>();
		
		
		
		cells = new FlowCell[GRID_SIZE][GRID_SIZE];
		for(int i=0;i<GRID_SIZE;i++)
			for(int j=0;j<GRID_SIZE;j++)
				cells[i][j] = new FlowCell(this,worldMap.getCell(i, j));	
		registerNeighours();
	}
	public void recalculate()
	{
		openCellatPos(goalPosition, initialCellsToOpen);
	}
	/*public void setCellAsGoal(FlowCell c)
	{
		c.isGoal=true;
		goals.add(c);
	}*/
	public void updateImage()
	{
		for(int i=0;i<GRID_SIZE;i++)
			for(int j=0;j<GRID_SIZE;j++)
			{
				image.setPixel(i, j, cells[i][j].getFlowColor());
				if( cells[i][j].flow.x>9999999||cells[i][j].flow.y>9999999||cells[i][j].flow.x<-9999999||cells[i][j].flow.y<-9999999)
					image.setPixel(i, j, Color.RED);
			}
		
		try {texture.loadFromImage(image);} 
		catch (TextureCreationException e) {e.printStackTrace();}
		
		shape.setTexture(texture);
	}
	/*public void openCells(Vector2f pos)
	{
		cellsToOpen=0;
		if(cellPosExists(pos.x,pos.y))
		{
			goalPosition = pos;
			resetIntegration();
			for(int i=0;i<GRID_SIZE;i++)
				for(int j=0;j<GRID_SIZE;j++)
					cells[i][j].isGoal=false;
			
		}
	}*/
	public void openCellatPos(Vector2f pos, int range)
	{
		cellsToOpen=range-1;
		if(cellPosExists(pos.x,pos.y))
		{
			goalPosition = pos;
			initialCellsToOpen=range;
			
			resetIntegration();
			for(int i=0;i<GRID_SIZE;i++)
				for(int j=0;j<GRID_SIZE;j++)
					cells[i][j].isGoal=false;
			//for(int i=pos.x/CELL_SIZE-range;i<=pos.x/CELL_SIZE+range;i++)
			//	for(int j=pos.y/CELL_SIZE-range;j<=pos.y/CELL_SIZE+range;j++)			
			//	{			
					int i=(int) (pos.x/CELL_SIZE);
					int j=(int) (pos.y/CELL_SIZE);
					//if(!cellExists(i, j))
					//	continue;
					goal =getCell(i,j);
					if(goal.neighbours.isEmpty())
						System.out.println("Target has no neighbours");
					goal.integration=0;
					goal.isGoal=true;
					openList.add(goal);
			//	}
			
			openAdjacentCells();
			losPass();
			while(openList.size()>0)
				integrate();
			setVectors();
			updateImage();
		}
		else
			System.out.println("Invalid Move Target");
	}
	public void openCellLocations(ArrayList<Vector2f> list)
	{
		cellsToOpen=0;
		resetIntegration();
		for(int i=0;i<GRID_SIZE;i++)
			for(int j=0;j<GRID_SIZE;j++)
				cells[i][j].isGoal=false;
		
		for(Vector2f v:list)
		{
			if(cellPosExists(v.x,v.y))
			{
				FlowCell goal =getCell((int)v.x,(int)v.y);
				goal.integration=0;
				goal.isGoal=true;
				openList.add(goal);
			}
		}
		losPass();
		while(openList.size()>0)
			integrate();
		setVectors();
		updateImage();
	}
	public void registerNeighours()
	{
		for(int i=0;i<GRID_SIZE;i++)
			for(int j=0;j<GRID_SIZE;j++)
			{
				if(cells[i][j].isOpen())
				{
					try{if(cells[i-1][j].isOpen())  cells[i][j].addNeighbour(cells[i-1][j]);}catch(IndexOutOfBoundsException ex){};
					try{if(cells[i+1][j].isOpen())  cells[i][j].addNeighbour(cells[i+1][j]);}catch(IndexOutOfBoundsException ex){};
					try{if(cells[i][j-1].isOpen())  cells[i][j].addNeighbour(cells[i][j-1]);}catch(IndexOutOfBoundsException ex){};
					try{if(cells[i][j+1].isOpen())  cells[i][j].addNeighbour(cells[i][j+1]);}catch(IndexOutOfBoundsException ex){};					
				}
			}
	}
	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) 
	{			
		arg0.draw(shape);
		
		for(int i=0;i<GRID_SIZE;i++)
			for(int j=0;j<GRID_SIZE;j++)
			{
				arg0.draw(cells[i][j].getVectorVisualistation());
				
				/*Text intnum = new Text(cells[i][j].toString(),Main.font);
				intnum.setCharacterSize(8);
				intnum.setPosition(cells[i][j].getPosition());
				arg0.draw(intnum);*/
				arg0.draw(cells[i][j].integrationText);
			}
				//cells[i][j].draw(arg0,Main.displayField);		
	}
	public void losPass()
	{
		outer:for(int i=0;i<GRID_SIZE;i++)
		{
			inner:for(int j=0;j<GRID_SIZE;j++)
			{
				//NEW CODE START
				FlowCell cell =cells[i][j];
				
				ArrayList<FlowCell> losCandidates = new ArrayList<FlowCell>();
				for(int x=0;x<GRID_SIZE;x++)
				{
					for(int y=0;y<GRID_SIZE;y++)
					{						
						if(cells[x][y].isGoal && cells[x][y]!=cell && hasLineOfSight(cell,cells[x][y]))
						{							
							losCandidates.add(cells[x][y]);
						}
					}
				}
				if(losCandidates.size()<1)
					continue;
				FlowCell targetCell=losCandidates.get(0);
				double minDist = (CommonFunctions.getDist(cell.getCenter(),targetCell.getCenter())/CELL_SIZE);
				for(FlowCell tempCell:losCandidates)
				{
					double dist = (CommonFunctions.getDist(cell.getCenter(), tempCell.getCenter())/CELL_SIZE);
					if(dist<minDist)
					{
						targetCell=tempCell;
						minDist=dist;
					}
				}
				cell.los=true;
				cell.setTargetCell(targetCell);
				cell.integration=(int)minDist;
				//NEW CODE END
				/*if(hasLineOfSight(getCellAtPos(goalPosition),cells[i][j]))
				{
					FlowCell c= cells[i][j];
					c.los=true;					
					c.integration=((int)CommonFunctions.getDist(cells[i][j].getCenter(), getCellAtPos(goalPosition).getCenter())/CELL_SIZE);
				}*/
			}
		}
	}
	public void openAdjacentCells()
	{
		outer:for(FlowCell c : openList.get(0).neighbours)
		{
			if(!closedList.contains(c)&&!openList.contains(c))
			{
				openList.add(c);		
				if(cellsToOpen-- >0) 
				{
					c.integration=0;
					c.isGoal=true;
				}
				else 
				{
					System.out.println(cellsToOpen);
					break outer;
				}
			}
			else if(closedList.contains(c))
			{
				if(c.integration > openList.get(0).integration + c.cost&&!c.los)
				{
					c.integration = openList.get(0).integration + c.cost;
				
					closedList.remove(c);
					openList.add(c);
				}
			}
			else if(openList.contains(c))
			{
				if(c.integration > openList.get(0).integration + c.cost)
				{
					c.integration = openList.get(0).integration + c.cost;
				}
			}
		}
	}
	public void integrate()
	{
		for(FlowCell c : openList.get(0).neighbours)
		{
			if(!closedList.contains(c)&&!openList.contains(c))
			{
				openList.add(c);		
				/*if(cellsToOpen-- >0) 
					{
						c.integration=0;
						c.isGoal=true;
					}
				else
				{*/
					if(!c.los)
						c.integration = openList.get(0).integration + c.cost;
				//	if(hasLineOfSight(getCellAtPos(goalPosition),c))
				//	{
				//		c.los=true;
					//	c.integration=(int) CommonFunctions.getDist(c.getCenter(), getCellAtPos(goalPosition).getCenter());
				//	}
				//}
			}
			else if(closedList.contains(c))
			{
				if(c.integration > openList.get(0).integration + c.cost&&!c.los)
				{
					c.integration = openList.get(0).integration + c.cost;
				
					closedList.remove(c);
					openList.add(c);
				}
			}
			else if(openList.contains(c))
			{
				if(c.integration > openList.get(0).integration + c.cost)
				{
					c.integration = openList.get(0).integration + c.cost;
				}
			}
		}
		closedList.add(openList.get(0));
		openList.remove(0);	
	}
	public boolean hasLineOfSight(FlowCell goal,FlowCell c)
	{
		boolean los=true;
		
		ArrayList<int[]> pts = CommonFunctions.getBresenhamLine(goal.x, goal.y,c.x , c.y);
		for(int i=0;i<pts.size()&&los;i++)
		{
			int p[] = pts.get(i);
			FlowCell cell=cells[p[0]][p[1]];
			if(!cell.isOpen())
			{
					los=false;
			}
			if(i>0&&!hasDiagonalConnection(p, pts.get(i-1)))
				los=false;
		}
		
		return los;
	}
	public boolean hasDiagonalConnection(int[] p1,int[] p2)
	{
		if(getCell(p1[0],p1[1]).neighbours.contains(getCell(p2[0],p2[1])))
			return true;
		for(FlowCell c:getCell(p1[0],p1[1]).neighbours)
		{
			if(c.neighbours.contains(getCell(p2[0],p2[1])))
				return true;
		}
		return false;
		
	}
	public void resetIntegration()
	{
		for(int i=0;i<GRID_SIZE;i++)
		{		
			for(int j=0;j<GRID_SIZE;j++)
			{
				cells[i][j].integration=255;
				cells[i][j].resetFlow();
			}
		}
		openList.clear();
		closedList.clear();
	}
	public void setVectors()
	{		
		/*for(int i=0;i<GRID_SIZE;i++)
		{
			for(int j=0;j<GRID_SIZE;j++)
			{
				if(cells[i][j].los)cells[i][j].integration=((int)CommonFunctions.getDist(cells[i][j].getCenter(), getCellAtPos(goalPosition).getCenter())/CELL_SIZE);
			}
		}*/
		ArrayList<FlowCell> cellsList = new ArrayList<FlowCell>();
		for(int i=0;i<GRID_SIZE;i++)
		{
			for(int j=0;j<GRID_SIZE;j++)
			{			
				if(cells[i][j].isOpen())cellsList.add(cells[i][j]);
			}
		}
		Collections.sort(cellsList, new Comparator<FlowCell>(){
			 @Override
		        public int compare(FlowCell  c1, FlowCell  c2)
		        {
		            return  c1.integration - c2.integration;
		        }
		});
		
		for(int i=0;i<GRID_SIZE;i++)
		{
			for(int j=0;j<GRID_SIZE;j++)
			{
				if(cells[i][j].los==true)
					continue;
				int[] values = new int[4];
				try{values[0]=cells[i][j-1].integration;}  catch(IndexOutOfBoundsException ex){values[0]=255;};
				//try{values[1]=cells[i+1][j-1].integration;}catch(IndexOutOfBoundsException ex){values[1]=255;};
				try{values[1]=cells[i+1][j].integration;}  catch(IndexOutOfBoundsException ex){values[1]=255;};
				//{values[3]=cells[i+1][j+1].integration;}catch(IndexOutOfBoundsException ex){values[3]=255;};
				try{values[2]=cells[i][j+1].integration;}  catch(IndexOutOfBoundsException ex){values[2]=255;};
				//try{values[5]=cells[i-1][j+1].integration;}catch(IndexOutOfBoundsException ex){values[5]=255;};
				try{values[3]=cells[i-1][j].integration;}  catch(IndexOutOfBoundsException ex){values[3]=255;};
				//try{values[7]=cells[i-1][j-1].integration;}catch(IndexOutOfBoundsException ex){values[7]=255;};
				int dir = getMinIndex(values);
				try{
					if(dir==0)cells[i][j].setTargetCell(cells[i][j-1]);
					else if(dir==1)cells[i][j].setTargetCell(cells[i+1][j]);
					else if(dir==2)cells[i][j].setTargetCell(cells[i][j+1]);
					else if(dir==3)cells[i][j].setTargetCell(cells[i-1][j]);
				}
				catch(IndexOutOfBoundsException ex){}
				if(cells[i][j].targetCell==null)
					System.out.println("failed");
			}
		}
		for(FlowCell c:cellsList)
			c.setDir();
	}
	public int getMinIndex(int[] array)
	{
		int minIndex=0;
		for(int i=0;i<array.length;i++)
		{
			if(array[i]<array[minIndex])
				minIndex=i;
		}
		/*if(directions[0]!=directions[1]&&(directions[1]==minIndex)&&array[directions[0]]<=array[minIndex])
		{
			if(Math.abs(directions[0]-directions[1])==1|| Math.abs(directions[0]-directions[1])==3)		
				return directions;
		}*/
		return minIndex;
	}
	public Vector2f getFlowAtPos(Vector2f pos) {
		try{
			if(getCellAtPos(pos).isGoal)
				return new Vector2f(0,0);
		return getCellAtPos(pos).flow;
		}
		catch(IndexOutOfBoundsException ex)
		{return new Vector2f(0,0);}
	}
	public FlowCell getCellAtPos(Vector2f pos) throws IndexOutOfBoundsException
	{
		return cells[(int) (pos.x/CELL_SIZE)][(int) (pos.y/CELL_SIZE)];
	}
	public FlowCell getCell(int x,int y) throws IndexOutOfBoundsException
	{
		return cells[x][y];
	}
	public boolean cellExists(int x,int y)
	{
		return (x>=0&&y>=0&&x<GRID_SIZE&&y<GRID_SIZE);		
	}
	public boolean cellPosExists(float x,float y)
	{
		return (x>=0&&y>=0&&x<GRID_SIZE*CELL_SIZE&&y<GRID_SIZE*CELL_SIZE);		
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		update();
	}
	public void update()
	{
		for(int i=0;i<GRID_SIZE;i++)
			for(int j=0;j<GRID_SIZE;j++)
				cells[i][j] = new FlowCell(this,Main.worldMap.getCell(i, j));	
		registerNeighours();
		recalculate();
	}
}
