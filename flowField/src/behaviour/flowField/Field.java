package behaviour.flowField;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.spi.LocaleServiceProvider;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.Image;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.RenderTexture;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Text;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import FYP.Main;
import common.CommonFunctions;
import gameElements.buildings.Building;
import gameElements.map.Map;

import static common.Constants.*;

public class Field  implements Drawable,Observer{
	//public static final int CELL_SIZE=20;
	//public static final int GRID_SIZE=100;
	//static final int GRID_SIZE = Main.resolution.x/CELL_SIZE;
	//static ExecutorService threadPool = Executors.newFixedThreadPool(1);
	Sprite sprite= new Sprite();
	Image image;
	Texture texture;
	RectangleShape shape;
	String fieldMode="";
	ArrayList<FlowCell> openList;
	ArrayList<FlowCell> closedList;
	ArrayList<FlowCell> borderList;
	FlowCell[][] cells;
	public boolean calculated=false;
	FlowCell goal;
	ArrayList<FlowCell> goals = new ArrayList<FlowCell>();
	
	int range;
	float minX, maxX,  minY, maxY;
	ArrayList<Vector2f> pointList;
	public Vector2f goalPosition;
	Building b;
	int initialCellsToOpen=0;
	public static final Field nullField=new NullField(Main.worldMap);
	int cellsToOpen; 
	RenderTexture rendTex;
	int bresenhamLineCalls=0; 
	/*public static init(int CELL_SIZE,int GRID_SIZE)
	{
		this.CELL_SIZE = CELL_SIZE;
		this.GRID_SIZE = GRID_SIZE;
	}*/
	
	public Field(Map worldMap)
	{
		
		worldMap.addObserver(this);
		
		goalPosition=new Vector2f(-1,0);
		
		openList = new ArrayList<FlowCell>();
		closedList = new ArrayList<FlowCell>();
		borderList = new ArrayList<FlowCell>();
		
		

		int counter=0;
		cells = new FlowCell[GRID_SIZE][GRID_SIZE];
		init(worldMap);
		//if()updateImage();
	}
	public void init(Map worldMap)
	{
		for(int i=0;i<GRID_SIZE;i++)
			for(int j=0;j<GRID_SIZE;j++)
				cells[i][j] = new FlowCell(this,worldMap.getCell(i, j));	
		registerNeighours();
		
		for (int i = 0; i < GRID_SIZE; i++) 
			for (int j = 0; j < GRID_SIZE; j++) 
			{
				if(!cells[i][j].isOpen())
				{
					for(int x=-1;x<=1;x++)
						for(int y=-1;y<=1;y++)
						{
							if(x!=0&&y!=0)
							{
								try{cells[i+x][j+y].increaseCost(3);}
								catch(Exception e){/*e.printStackTrace();*/}
							}
						}
				}
			}
	}
	public void recalculate()
	{
		calculated=false;
		Thread t = new Thread(new Runnable(){
			@Override
			public void run() {
				if(fieldMode.equals("clickPoint"))openCellatPos(goalPosition, range,minX,maxX, minY,maxY);		
				else if(fieldMode.equals("pointList"))openCellLocations(pointList);
			}
		});
		t.start();	
	}
	/*public void setCellAsGoal(FlowCell c)
	{
		c.isGoal=true;
		goals.add(c);
	}*/
	public void updateImage()
	{
		if(image==null)
		{
			image = new Image();
			texture = new Texture();
			if(rendTex==null)rendTex=new RenderTexture();
			image.create(GRID_SIZE*CELL_SIZE,GRID_SIZE*CELL_SIZE);
	
			shape = new RectangleShape(new Vector2f(GRID_SIZE*CELL_SIZE,GRID_SIZE*CELL_SIZE));
		

			try {
				rendTex.create((GRID_SIZE*CELL_SIZE),(GRID_SIZE*CELL_SIZE));
			} catch (TextureCreationException e) {e.printStackTrace();}
			sprite.setTexture(rendTex.getTexture());
		}
		/*Image img = new Image();
		img.create(GRID_SIZE,GRID_SIZE);
		for(int i=0;i<GRID_SIZE;i++)
			for(int j=0;j<GRID_SIZE;j++)
			{
				img.setPixel(i, j, cells[i][j].getFlowColor());
				if( cells[i][j].flow.x>9999999||cells[i][j].flow.y>9999999||cells[i][j].flow.x<-9999999||cells[i][j].flow.y<-9999999)
					img.setPixel(i, j, Color.RED);
			}
		image.copy(img, 0, 0);
		try {texture.loadFromImage(image);} 
		catch (TextureCreationException e) {e.printStackTrace();}
		texture.setRepeated(true);
		shape.setTexture(texture);
		
		shape.setFillColor(Color.TRANSPARENT);
		
		*/
		if(SHOW_FLOW)
		{
			rendTex.clear(Color.TRANSPARENT);
			
			for(int i=0;i<GRID_SIZE;i++)
				for(int j=0;j<GRID_SIZE;j++)
				{
					if(cells[i][j].isValid&&cells[i][j].isOpen()&&!cells[i][j].isGoal)
					{
						rendTex.draw(cells[i][j].getVectorVisualistation());
						rendTex.draw(cells[i][j].integrationText);
					}
				}
			rendTex.display();
		}
		//shape.setTexture(rendTex.getTexture());
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
	public void openCellatPos(Vector2f pos, int range,float minX,float maxX, float minY,float maxY)
	{
		cellsToOpen=range;
		fieldMode="clickPoint";
		this.range=range;
		this.minX=minX;
		this.maxX=maxX;
		this.minY=minY;
		this.maxY=maxY;
		
		int tolerance = 500;
		int max[] = new int[]{(int) ((maxX+tolerance)/CELL_SIZE),(int) ((maxY+tolerance)/CELL_SIZE)};
		int min[] = new int[]{(int) ((minX-tolerance)/CELL_SIZE),(int) ((minY-tolerance)/CELL_SIZE)};
		//System.out.println("maxX:"+max[0] +" , maxY:"+max[1]);
		//System.out.println("minX:"+min[0] +" , minY:"+min[1]);
		for(int i=0;i<GRID_SIZE;i++)
			for(int j=0;j<GRID_SIZE;j++)
			{
				if((i>min[0]&&i<max[0]) && (j>min[1]&&j<max[1]))
				{
					cells[i][j].isValid=true;
				}
				else
					cells[i][j].isValid=false;
			}
		openCellatPos(pos);
	}
	public void openCellatPos(Vector2f pos)
	{
		if(cellPosExists(pos.x,pos.y))
		{
			goalPosition = pos;
			
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
			//while(cellsToOpen>0) TODO more computation but bigger area
			openAdjacentCells();
			losPass();
			System.out.println(bresenhamLineCalls);
			while(openList.size()>0)
				try{integrate();}catch(NullPointerException ex){}

			setVectors();
			if(SHOW_FLOW)updateImage();
			System.out.println(borderList.size());
			calculated=true;
		}
		else
			System.out.println("Invalid Move Target");
	}	
	public void openCellLocations(ArrayList<Vector2f> list)
	{
		pointList=list;
		fieldMode="pointList";
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
			try{integrate();}catch(NullPointerException ex){}
		setVectors();
		if(SHOW_FLOW)updateImage();

		calculated=true;
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
		/*try	{
		sprite.setTexture(rendTex.getTexture());}
		catch(NullPointerException ex){updateImage();sprite.setTexture(rendTex.getTexture());}
		*/arg0.draw(sprite);		
	}
	public void losPass()
	{
		ArrayList<FlowCell> temp = new ArrayList<FlowCell>();
		ArrayList<FlowCell> losCandidates = new ArrayList<FlowCell>();
		for(int x=0;x<GRID_SIZE;x++)
		{
			for(int y=0;y<GRID_SIZE;y++)
			{
				if(cells[x][y].isGoal)
					losCandidates.add(cells[x][y]);
			}
		}
		outer:for(int i=0;i<GRID_SIZE;i++)
		{
			inner:for(int j=0;j<GRID_SIZE;j++)
			{
				FlowCell cell =cells[i][j];
				if(!cell.isValid||!cell.isOpen()||cell.los)
					continue;
				
				/*ArrayList<FlowCell> losCandidates = new ArrayList<FlowCell>();
				for(int x=0;x<GRID_SIZE;x++)
				{
					for(int y=0;y<GRID_SIZE;y++)
					{
						if(cells[x][y].isGoal)
							losCandidates.add(cells[x][y]);
					}
				}*/
				
				Collections.sort(losCandidates, new Comparator<FlowCell>(){
					 @Override
				        public int compare(FlowCell  c1, FlowCell  c2)
				        {
				            return  (int)(CommonFunctions.getDistSqr(c1.getCenter(), cell.getCenter()) - CommonFunctions.getDistSqr(c2.getCenter(), cell.getCenter()));
				        }
				});
				boolean found=false;
				for(int x=0;!found&&x<losCandidates.size();x++)	
				{
					FlowCell f=losCandidates.get(x);
					if(f!=cell)
					{		
						if(hasLineOfSightWithAssignment(cell,f))
							found=true;
						else			
						{
							losCandidates.remove(f);
							temp.add(f);
						}
					}
					
				}
				for(FlowCell f:temp)
				{
					losCandidates.add(f);
				}
				temp.clear();
				
				if(!found)
					continue;
					//FlowCell targetCell=losCandidates.get(0);
				/*double minDist = (CommonFunctions.getDist(cell.getCenter(),targetCell.getCenter())/CELL_SIZE);
				for(FlowCell tempCell:losCandidates)
				{
					double dist = (CommonFunctions.getDist(cell.getCenter(), tempCell.getCenter())/CELL_SIZE);
					if(dist<minDist)
					{
						targetCell=tempCell;
						minDist=dist;
					}
				}*/
				/*cell.los=true;
				cell.setTargetCell(targetCell);
				cell.integration=(int)(CommonFunctions.getDist(cell.getCenter(),targetCell.getCenter())/CELL_SIZE)+cell.cost;
				*/
					//cell.enableLos(targetCell);
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
		while(cellsToOpen>0)
		{
			outer:for(FlowCell c : openList.get(0).neighbours)//TODO sort neighbours by distance to target
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
						//System.out.println(cellsToOpen);
						break outer;
					}
				}	
			}
			closedList.add(openList.get(0));
			openList.remove(0);	
		}	
	}
	public void integrate() throws NullPointerException
	{
		try{
			for(FlowCell c : openList.get(0).neighbours)
			{
				try{
					if(!closedList.contains(c)&&!openList.contains(c))
					{
						if(c.isValid)
						{
							openList.add(c);	
		
							if(!c.los)
								c.integration = openList.get(0).integration + c.cost;
						}
						else
							borderList.add(c);
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
				}catch(Exception e){}
			}
		}catch(Exception e){}
		if(!openList.isEmpty())
		{
			closedList.add(openList.get(0));
			try{openList.remove(0);}	catch(ArrayIndexOutOfBoundsException ex){}
		}
	}
	public boolean hasLineOfSight(FlowCell goal,FlowCell c)
	{
		boolean los=true;
		
		ArrayList<int[]> pts = CommonFunctions.getBresenhamLine(goal.x, goal.y,c.x , c.y);
		bresenhamLineCalls++;
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
	public boolean hasLineOfSightWithAssignment(FlowCell goal,FlowCell c)
	{
		boolean los=true;
		
		ArrayList<int[]> pts = CommonFunctions.getBresenhamLine(goal.x, goal.y,c.x , c.y);
		bresenhamLineCalls++;
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
		if(los)
		{
			for(int i=0;i<pts.size()&&los;i++)
			{
				int p[] = pts.get(i);
				cells[p[0]][p[1]].enableLos(c);
			}
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
				if(cells[i][j].isValid&&cells[i][j].isOpen())
					cellsList.add(cells[i][j]);
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
				if(cells[i][j].los==true || !cells[i][j].isValid || !cells[i][j].isOpen())
					continue;
				int[] values = new int[4];
				try{values[0]=cells[i][j-1].integration;}  catch(IndexOutOfBoundsException ex){values[0]=255;};
				try{values[1]=cells[i+1][j].integration;}  catch(IndexOutOfBoundsException ex){values[1]=255;};
				try{values[2]=cells[i][j+1].integration;}  catch(IndexOutOfBoundsException ex){values[2]=255;};
				try{values[3]=cells[i-1][j].integration;}  catch(IndexOutOfBoundsException ex){values[3]=255;};
				int dir = getMinIndex(values);
				try{
					if(dir==0)cells[i][j].setTargetCell(cells[i][j-1]);
					else if(dir==1)cells[i][j].setTargetCell(cells[i+1][j]);
					else if(dir==2)cells[i][j].setTargetCell(cells[i][j+1]);
					else if(dir==3)cells[i][j].setTargetCell(cells[i-1][j]);
				}
				catch(IndexOutOfBoundsException ex){}
				//if(cells[i][j].targetCell==null)
					//System.out.println("failed");
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
		init(Main.worldMap);
		recalculate();
	}
}
