package JORTS.core;

import static JORTS.common.Constants.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Keyboard.Key;

import JORTS.behaviour.flowField.Field;
import JORTS.behaviour.orders.AttackBuildingOrder;
import JORTS.behaviour.orders.ConstructBuildingOrder;
import JORTS.behaviour.orders.DepositResourcesOrder;
import JORTS.behaviour.orders.FollowOrder;
import JORTS.behaviour.orders.HarvestResourceOrder;
import JORTS.behaviour.orders.MoveOrder;
import JORTS.behaviour.orders.Order;
import JORTS.common.CommonFunctions;
import JORTS.gameElements.buildings.Building;
import JORTS.gameElements.buildings.resources.Resource;
import JORTS.gameElements.map.MapCell;
import JORTS.gameElements.map.VisionMap;
import JORTS.gameElements.units.Entity;
import JORTS.uiComponents.textFields.ResourceList;
public class Player implements Drawable,Observer{ 
	public String name="";
	Color teamColor;
	public Field lastField;
	ArrayList<Entity> units;
	ArrayList<Building> buildings;
	
	public static int unitCap=300;
	public int unitCount=0;
	
	public VisionMap visionMap;
	
	ArrayList<Entity> selectedUnits;
	ArrayList<Building> selectedBuildings;
	public Field currentField=Field.nullField;
	public Vector2f selectionPoint;
	public boolean selectionInProgress=false;
	public static Color playerColors[] = new Color[]{new Color(150,150,150),Color.BLUE,Color.RED,Color.MAGENTA,new Color(150,0,150)};
	private static Vector2f offset = new Vector2f(200,300);
	public static Vector2f spawnPoints[] = new Vector2f[]{
			//new Vector2f(offset.x,offset.y),
			//new Vector2f(offset.x,offset.y),
		//	new Vector2f(offset.x,offset.y),
			new Vector2f(offset.x,offset.y),
			new Vector2f((CELL_SIZE*GRID_SIZE)-offset.x,(CELL_SIZE*GRID_SIZE)-offset.y),
			new Vector2f(offset.x,(CELL_SIZE*GRID_SIZE)-offset.y),
			new Vector2f((CELL_SIZE*GRID_SIZE)-offset.x,offset.y)
	};
	public static int players;
	public int playerNum;
	public Color color;
	public Vector2f startPosition;
	public TreeMap<String,Integer> resources;
	public Player(String name)
	{
		resources=new TreeMap<String,Integer>(String.CASE_INSENSITIVE_ORDER);
		for(String s:ResourceList.resources)
			resources.put(s,0);
		playerNum=players++;
		this.name=name;//"player_"+playerNum;
		color=playerColors[playerNum];
		startPosition=spawnPoints[playerNum];		
		
		selectionPoint = Vector2f.ZERO;
		units=new ArrayList<Entity>();
		buildings=new ArrayList<Building>();
		
		selectedUnits=new ArrayList<Entity>();
		selectedBuildings=new ArrayList<Building>();
		
		visionMap=new VisionMap(this);
		
		Main.game.addObserver(this);
	}
	public void startSelection(Vector2f v,GameWindow window)
	{
		window.gui.selectionRect.start(window.mouse.uiClickLoc);
		
		selectionPoint = v;
		selectionInProgress=true;
	}
	public void endSelection(Vector2f v,boolean doSelection,GameWindow window)
	{
		if(doSelection)
		{
			/*RectangleShape selectionRect = new RectangleShape();
			selectionRect.setPosition(selectionPoint.x, selectionPoint.y);
			selectionRect.setSize(new Vector2f(v.x-selectionPoint.x+1,  v.y-selectionPoint.y+1));
			*/selectUnits(window.gui.selectionRect.globalBounds);
			
			if(selectedUnits.size()>0&&window.mouse.doubleClick&&window.gui.selectionRect.globalBounds.width<1&&window.gui.selectionRect.globalBounds.height<1)
				selectUnitType(selectedUnits.get(0).getType());
						
			if(selectedUnits.size()==0)
			{
				selectBuildings(window.gui.selectionRect.globalBounds);
				if(selectedBuildings.size()>0&&window.mouse.doubleClick)
					selectBuildingType(selectedBuildings.get(0).getType());
			}
		}
		selectionInProgress=false;
		window.gui.selectionRect.end();
	}
	public void addUnit(Entity e)
	{
		if(unitCount<unitCap)
		{
			unitCount++;
			units.add(e);
			e.setPlayer(this);
		}
	}
	public void addBuilding(Building e)
	{
		buildings.add(e);
		e.setPlayer(this);
	}	
	public void selectUnitType(String type)
	{
		ArrayList<Entity> temp = new ArrayList<Entity>();
		GameWindow window = Main.getPlayerWindow(this);
		window.setView(window.gameView);
		for(Entity e:units)
		{
			if(e.getType().equals(type)&&CommonFunctions.isOnScreen(window, e))
			{
				temp.add(e);
			}
		}
		selectUnits(temp);
	}	
	public void selectBuildingType(String type)
	{
		ArrayList<Building> temp = new ArrayList<Building>();
		for(Building b:buildings)
		{
			if(b.getType().equals(type))
				temp.add(b);
		}
		selectBuildings(temp);
	}
	public void assignControlGroup(int ctrlGrp)
	{
		for(Entity e:units)
		{
			if(e.getControlGroup()==ctrlGrp)
				e.setControlGroup(-1);
		}
		for(Entity e:selectedUnits)
			e.setControlGroup(ctrlGrp);
	}
	public void selectControlGroup(int ctrlGrp)
	{
		ArrayList<Entity> temp = new ArrayList<Entity>();
		for(Entity e:units)
		{
			if(e.getControlGroup()==ctrlGrp)
				temp.add(e);		
		}
		selectUnits(temp);
	}
	public void selectUnits(FloatRect rect)
	{		
		ArrayList<Entity> temp = new ArrayList<Entity>();
		for(Entity e:units)
		{
			if(rect.contains(e.getPosition().x, e.getPosition().y))
				temp.add(e);
			else if(CommonFunctions.getDist(rect, e.getPosition())<e.getRadius())				
			{
				temp.add(e);
			}
				
		}
		selectUnits(temp);
	}
	public void selectUnit(Entity e)
	{
		ArrayList<Entity> es = new ArrayList<Entity>();
		es.add(e);
		selectUnits(es);
	}
	public void selectBuilding(Building b)
	{
		ArrayList<Building> es = new ArrayList<Building>();
		es.add(b);
		selectBuildings(es);
	}
	public void selectBuilding(String id)
	{
		Building e=Building.allBuildings.get(id);
		if(e.player==this)
			selectBuilding(e);
	}
	public void selectUnit(String id)
	{
		Entity e=Entity.allEntities.get(id);
		if(e.player==this)
			selectUnit(e);
	}
	public void selectUnits(ArrayList<Entity> es)
	{
		clearSelectedBuildings();
		if(!Keyboard.isKeyPressed(Key.LCONTROL))clearSelectedUnits();
		if(es.size()>0)
		{
			for(Entity e:es)
			{
				selectedUnits.add(e);
				e.select();
			}
			updateFlowfieldVisual();
			
		}
	}
	public void updateFlowfieldVisual()
	{
		HashMap<Order, Integer> types = new HashMap<Order, Integer>();				
		for(Entity e:selectedUnits)
		{
			if(!types.containsKey(e.currentOrder))
				types.put(e.currentOrder,1);
			else
				types.put(e.currentOrder,types.get(e.currentOrder)+1);
		}
		
		int most=0;
		Order o=null;
	    Iterator it = types.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        if((Integer)pair.getValue()>most)
	        {
	        	most=(Integer)pair.getValue();
	        	o=(Order)pair.getKey();
	        }
	    }
		currentField=o.flowField;
	}
	public void selectBuildings(ArrayList<Building> bs)
	{
		clearSelectedUnits();
		if(!Keyboard.isKeyPressed(Key.LCONTROL))clearSelectedBuildings();
		for(Building e:bs)
		{
			selectedBuildings.add(e);
			e.select();
		}
	}
	
	public void selectUnit(Vector2f pos)
	{
		FloatRect f = new FloatRect(pos,new Vector2f(1,1));
		selectUnits(f);
	}
	public void selectBuildings(FloatRect rect)
	{		
		ArrayList<Building> temp = new ArrayList<Building>();
		FloatRect r = new FloatRect(rect.left,rect.top,rect.width+1,rect.height+1);
		for(Building b:buildings)
		{
			if(b.getGlobalBounds().intersection(r)!=null)
				temp.add(b);				
		}
		selectBuildings(temp);
	}
	public void deselectUnit(Entity e)
	{
		selectedUnits.remove(e);
		e.select();
	}
	public void clearSelectedUnits()
	{
		for(Entity e:selectedUnits)
			e.deselect();
		selectedUnits.clear();
		currentField=Field.nullField;
	}
	private void clearSelectedBuildings() {
		for(Building b:selectedBuildings)
			b.deselect();
		selectedBuildings.clear();	
	}
	public void clearAllSelected()
	{
		clearSelectedBuildings();
		clearSelectedUnits();
	}
	public void issueMoveCommand(Vector2f loc)
	{
		if(selectedUnits.size()>0)
		{
			float minX,maxX,minY,maxY;
			minX=loc.x;
			maxX=loc.x;
			
			minY=loc.y;
			maxY=loc.y;
			
			for(Entity e:selectedUnits)
			{
				if(e.getPosition().x>maxX)
					maxX=e.getPosition().x;
				if(e.getPosition().x<minX)
					minX=e.getPosition().x;
				
				if(e.getPosition().y>maxY)
					maxY=e.getPosition().y;
				if(e.getPosition().y<minY)
					minY=e.getPosition().y;
			}
			
			MoveOrder m = new MoveOrder();
			double requiredArea=0;
			for(Entity e:selectedUnits)
				requiredArea+=((e.getRadius()+e.influenceRange)*(e.getRadius()+e.influenceRange)*2);
			int requiredCells=(int) (requiredArea/(CELL_SIZE*CELL_SIZE));
			
			m.init(loc,requiredCells,minX,maxX,minY,maxY);//TODO adapt area to unit size
			
			for(Entity e:selectedUnits)
			{
				m.issue(e);
			}
			updateFlowfieldVisual();
		}		
	}
	public void issueFollowCommand(Entity ent)
	{
		if(selectedUnits.size()>0)
		{
			FollowOrder m = new FollowOrder();
			//currentField=m.flowField;
			for(Entity e:selectedUnits)
			{
				m.issue(e);
			}

			m.init(ent);

			updateFlowfieldVisual();
		}
	}
	public void issueRallyPointOrder(Vector2f loc)
	{
		if(selectedBuildings.size()>0)
		{

			MoveOrder m = new MoveOrder();
			
			float minX,maxX,minY,maxY;
			minX=loc.x;
			maxX=loc.x;
			
			minY=loc.y;
			maxY=loc.y;
			
			for(Building b:selectedBuildings)
			{
				if(b.getPosition().x>maxX)
					maxX=b.getPosition().x;
				if(b.getPosition().x<minX)
					minX=b.getPosition().x;
				
				if(b.getPosition().y>maxY)
					maxY=b.getPosition().y;
				if(b.getPosition().y<minY)
					minY=b.getPosition().y;
			}
			m.init(loc,1,minX,maxX,minY,maxY);
			for(Building b:selectedBuildings)
			{
				b.setRallyPoint(m);
			}
		}
	}
	public void issueAttackBuildingOrder(Building b)
	{
		if(selectedUnits.size()>0)
		{
			System.out.println("attack");
			AttackBuildingOrder m = new AttackBuildingOrder(b.getSurroundOrder());
			//m.init(b);
			for(Entity e:selectedUnits)
			{
				//b.surroundOrder.issue(e);
				m.issue(e);
			}

			updateFlowfieldVisual();
		}
	}
	public void issueHarvestResourceOrder(Resource r)
	{
		if(!selectedUnits.isEmpty())
		{
			//HarvestResourceOrder m = r.harvestOrder;//new HarvestResourceOrder(r.surroundOrder);
			for(Entity e:selectedUnits)
			{
				r.getHarvestOrder().issue(e);
			}

			updateFlowfieldVisual();
		}
	}
	public void issueDepositResourceOrder(Building b)
	{
		if(!selectedUnits.isEmpty())
		{			
			DepositResourcesOrder m = new DepositResourcesOrder(b.getSurroundOrder());
			for(Entity e:selectedUnits)
			{
				if(e.heldResources!=null&&e.heldResources.amount>0)
					m.issue(e);
			}

			updateFlowfieldVisual();
		}
	}
	public void issueConstructBuildingOrder(Building b)
	{
		if(!selectedUnits.isEmpty()&&b.inConstruction)
		{
			System.out.println("attack");
			ConstructBuildingOrder m = new ConstructBuildingOrder(b.getSurroundOrder());
			//m.init(b);
			for(Entity e:selectedUnits)
			{
				if(e.canConstruct)
					m.issue(e);
			}

			updateFlowfieldVisual();
		}
	}
	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) {
		for(Entity e:units)
		{
			arg0.draw(e);			
		}
		for(Building b:buildings)
		{
			arg0.draw(b);
		}
	}
	public void tick()
	{
		if(!PAUSED)
		{
			for(Entity entity:units)
			   entity.tick();
			for(Entity entity:units)
			   entity.reregister();
			for(Building building:buildings)
				building.tick();
		//	/*if(Main.activePlayer==this&&SHOW_VISION_MASK)*/revealMap();//TODO every player has vision map
		}
	}
	@Override
	public void update(Observable o, Object arg) {
		try{Main.getPlayerWindow(this).gui.selectionRect.update(Main.getPlayerWindow(this).mouse.uiClickLoc);}//TODO fix this shit
		catch(Exception e){}
		tick();		
	}
	
	public void revealMap()
	{
		if(SHOW_VISION_MASK)
		{
			/*for(int i=0;i<GRID_SIZE;i++)
				for(int j=0;j<GRID_SIZE;j++)
				{
					if(Main.worldMap.cells[i][j].visible)
						Main.worldMap.cells[i][j].mask=true;
					Main.worldMap.cells[i][j].visible=false;
				}
			
	
			int visionRangePix=300;
			int visionRangePixSqr=visionRangePix*visionRangePix;
			int visionRangeCells=(visionRangePix/CELL_SIZE)+2;
			for(Entity e:units)
			{
				Vector2f ePos = e.getPosition();
				MapCell cell = Main.worldMap.getCellAtPos(ePos);
				
				for(int i=cell.x-visionRangeCells;i<cell.x+visionRangeCells;i++)
					for(int j=cell.y-visionRangeCells;j<cell.y+visionRangeCells;j++)
					{
						if(i>=0&&j>=0&&i<GRID_SIZE&&j<GRID_SIZE)
						{
							if(!Main.worldMap.cells[i][j].visible&&CommonFunctions.getDistSqr(Main.worldMap.cells[i][j].getPosition(), e.getPosition())<visionRangePixSqr)
							{
								Main.worldMap.cells[i][j].visible=true;
								Main.worldMap.cells[i][j].mask=false;
							}
						}
					}
			}*/
			visionMap.update();
		}
	}
	public ArrayList<Entity> getUnits()
	{
		return units;
	}
	public ArrayList<Entity> getSelectedUnits()
	{
		return selectedUnits;
	}
	public ArrayList<Building> getBuildings()
	{
		return buildings;
	}
	public ArrayList<Building> getSelectedBuildings()
	{
		return selectedBuildings;
	}
	public void collectResource(String resource,int amount)
	{
		resources.replace(resource,resources.get(resource)+amount);
	}
}
