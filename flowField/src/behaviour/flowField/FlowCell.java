package behaviour.flowField;

import java.util.ArrayList;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;

import FYP.Main;
import common.CommonFunctions;
import gameElements.map.MapCell;
import gameElements.units.Entity;

import static common.Constants.*;

public class FlowCell {
	public int x;
	public int y;
	int cost;
	public boolean los=false;
	public boolean isValid=true;
	Text integrationText;
	public boolean isGoal=false;
	public int integration;
	Vector2f flow;
	int angle=0;
	boolean open=true;
	Field field;
	public ArrayList<FlowCell> neighbours;
	//public ArrayList<Entity> entities;
	int vectorValue;
	FlowCell targetCell=null;
	RectangleShape vectorVisualisation;
	Vector2f position;
	Vector2f size;
	
	MapCell mpCl;
	public static float maxSpeed =.5f;
	
	public FlowCell(Field field,MapCell mpCl)
	{
		super();
		this.mpCl=mpCl;
		this.field=field;
		//entities = new ArrayList<Entity>();
		flow = new Vector2f(0,0);
		this.x=mpCl.x;
		this.y=mpCl.y;
		neighbours = new ArrayList<FlowCell>();
		cost=mpCl.cost;
		if(!mpCl.isTraversable())
			close();
		
		setSize(new Vector2f(CELL_SIZE,CELL_SIZE));
		setPosition(new Vector2f(CELL_SIZE*x,CELL_SIZE*y));
	}
	public void setTargetCell(FlowCell c)
	{
		targetCell=c;
	}
	public void setPosition(Vector2f v)
	{
		position=v;
	}
	public Vector2f getPosition()
	{
		return position;
	}
	public void setSize(Vector2f v)
	{
		size=v;
	}
	public Vector2f getSize()
	{
		return size;
	}
	public RectangleShape getVectorVisualistation()
	{
		if(vectorVisualisation==null)
		{
			integrationText = new Text();
			vectorVisualisation=new RectangleShape();
			updateVectorVisualisation();
		}
		return vectorVisualisation;
	}
	public FloatRect getGlobalBounds()
	{
		FloatRect r = new FloatRect(getPosition(),getSize());
		return r;
	}
	public void increaseCost(int cost)
	{
		this.cost+=cost;
	}
	public void addNeighbour(FlowCell c)
	{
		neighbours.add(c);
	}
	public Vector2f getCenter()
	{
		return Vector2f.add(getPosition(), new Vector2f(CELL_SIZE/2,CELL_SIZE/2));
	}
	public void open()
	{		
		open=true;
	}
	public void close()
	{		
		open=false;
	}
	public boolean isOpen()
	{		
		return open;
	}
	public Color getFlowColor()
	{
		if(isGoal) return Color.YELLOW;
		if(los) return Color.CYAN;
		int shade =255-( Math.min(255,(integration*5)));
		return new Color(shade,shade,shade,200);
	}
	public void resetFlow()
	{
		flow = new Vector2f(0,0);
	}
	public void setDir()//vector)
	{
		resetFlow();
		if (los&&!isGoal)
			flow=Vector2f.sub(targetCell.getCenter(),getCenter());
		else if(integration==255)
		{
			flow = new Vector2f(0,0);			
		}
		else if(targetCell!=null)
		{
			flow=Vector2f.sub(targetCell.getCenter(),getCenter());
			flow = CommonFunctions.normaliseVector(flow);
			
			flow = Vector2f.add(flow, Vector2f.mul(targetCell.flow,2));
			if(targetCell.flow.x==0&&targetCell.flow.y==0)
			{
			//System.out.println("error?");
			}
		}
		//if(flow.x!=0&&flow.y!=0)
		//{
			flow = CommonFunctions.normaliseVector(flow);
			flow = Vector2f.mul(flow, maxSpeed);		
		//}
		if(SHOW_FLOW)updateVectorVisualisation();
	}
	public void enableLos(FlowCell target)
	{
		if(!los)
		{
			los=true;
			setTargetCell(target);
			integration=(int)(CommonFunctions.getDist(getCenter(),target.getCenter())/CELL_SIZE)+cost;
		}
	}
	public void updateVectorVisualisation()
	{
		if(vectorVisualisation==null)
		{
			vectorVisualisation=new RectangleShape();
			integrationText = new Text();
		}
		vectorVisualisation.setPosition(this.getCenter());
		vectorVisualisation.setSize(new Vector2f(0,CELL_SIZE/2));
		vectorVisualisation.setOutlineThickness(1);
		vectorVisualisation.setOutlineColor(Color.BLACK);
		float a = CommonFunctions.getAngle(flow,new Vector2f(0, 0));
		//System.out.println(flow.x+","+flow.y);
		//System.out.println(a);
		vectorVisualisation.rotate(a);
		
		integrationText = new Text(this.toString(),Main.font);
		integrationText.setCharacterSize(20);
		if(los)integrationText.setColor(Color.BLUE);
		else integrationText.setColor(Color.RED);
		integrationText.setPosition(this.getPosition());
	}
	public String toString()
	{
		//cost=mpCl.cost;
		//String result=((int)integration)+"\n";
		//String result=((int)cost)+"\n";
		String result=neighbours.size()+"\n";
		
		//result += flow.x+",\n"+flow.y;
		//String result=cost+"";
		//String result=((int)integration)+"";
		//String result=(Main.worldMap.getCell(x, y).getEntities().size())+"";
		return result;
	}
}
