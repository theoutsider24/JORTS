package JORTS.gameElements.buildings;

import static JORTS.common.Constants.CELL_SIZE;
import static JORTS.common.Constants.SHOW_HEALTH_BARS;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsfml.graphics.CircleShape;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;

import JORTS.behaviour.Attackable;
import JORTS.behaviour.abilities.Ability;
import JORTS.behaviour.orders.MoveOrder;
import JORTS.behaviour.orders.SurroundBuildingOrder;
import JORTS.behaviour.timedBehaviours.ProductionTimedBehaviour;
import JORTS.common.CommonFunctions;
import JORTS.common.ResourceAmount;
import JORTS.core.GameWindow;
import JORTS.core.Main;
import JORTS.core.Player;
import JORTS.gameElements.map.MapCell;
import JORTS.uiComponents.HealthBar;

public abstract class Building extends RectangleShape implements Attackable{
	public static HashMap<String,Building> allBuildings = new HashMap<String,Building>();
	public ArrayList<int[]> offsets = new ArrayList<int[]>(); 
	public boolean placed=false;
	public boolean valid=true;
	public boolean destroyed=false;
	public int visionRange;
	public MoveOrder rallyOrder;
	public int[] origin=new int[2];
	public int maxQueueSize=6;
	public ArrayList<MapCell> occupiedCells = new ArrayList<MapCell>();
	public ArrayList<Ability> abilities = new ArrayList<Ability>();
	//public ArrayList<CreateUnitAbility> productionQueue;
	public ArrayList<ProductionTimedBehaviour> productionQueue;
	public String id;
	public static int numberOfBuildings;
	public String buildingType="building";
	public Player player;
	Color playerColor=new Color(50,50,50,0);
	boolean selected=false;
	private SurroundBuildingOrder surroundOrder;
	int outlineThickness=2;
	public boolean healthBarEnabled=true;
	protected HealthBar healthBar;
	public int maxHealth;
	public int health;
	
	public ArrayList<String> resourceRepository=new ArrayList<String>();
	
	public boolean inConstruction=true;
	
	boolean acting=false;
	
	ArrayList<MapCell> spawnLocations=new ArrayList<MapCell>();
	
	public int dimension=5;
	public Building(int size)
	{
		dimension=size;
		id=buildingType+"_#"+numberOfBuildings++;
		
		for(int i=0;i<dimension&&offsets.size()<(dimension*dimension);i++)
			for(int j=0;j<dimension&&offsets.size()<(dimension*dimension);j++)
			{
				offsets.add(new int[]{i-(dimension/2),j-(dimension/2)});
			}
		
		//setFillColor(playerColor);
		setFillColor(new Color(255,255,255,100));
		
		setSize(new Vector2f((dimension*CELL_SIZE)-outlineThickness*2,(dimension*CELL_SIZE)-outlineThickness*2));
		
		productionQueue=new ArrayList<ProductionTimedBehaviour>();


		healthBar=new HealthBar(this);
		//setMaxHealth(100);
		//healthBar.setMaxHealth(maxHealth);
		//surroundOrder= new SurroundBuildingOrder();
	}
	public void setMaxHealth(int max)
	{
		maxHealth=max;
		health=max;
		healthBar.setMaxHealth(maxHealth);
	}
	public void setRallyPoint(MoveOrder rp)
	{
		rallyOrder=rp;
	}
	public void cancelProduction(int i)
	{
		try{

			for(ResourceAmount r:productionQueue.get(i).requiredResources)
				productionQueue.get(i).building.player.collectResource(r.resource, r.amount);
		productionQueue.get(i).stop();
		productionQueue.remove(i);
		}
		catch(Exception ex){}
	}
	public void tick()
	{
		if(productionQueue.size()>0&&!productionQueue.get(0).running)
		{
			if(productionQueue.get(0).timeToComplete<=0)productionQueue.remove(0);
			if(productionQueue.size()>0)
				productionQueue.get(0).start();
		}
		
	}
	public void startProduction(String type,int time,ArrayList<ResourceAmount> requiredResources)
	{
		if(productionQueue.size()<maxQueueSize)
		{
			productionQueue.add(
				new ProductionTimedBehaviour(time,type,this,requiredResources)
			);
		}
	}
	public void disableAbilities()
	{
		for(Ability a:abilities)
			a.disable();
	}
	public void enableAbilities()
	{
		for(Ability a:abilities)
			a.enable();
	}
	public void completeConstruction()
	{
		enableAbilities();
		inConstruction=false;
		setSpawnLocations();

		healthBar.setMaxHealth(maxHealth);
		updateHealth(maxHealth);
		healthBar.setToStandardColor();

		buildingType=buildingType.replace("_foundation","");
		
		if(getTexture()==null)setFillColor(playerColor);
		else	setFillColor(Color.WHITE);
	}
	public void placeFoundation()
	{
		buildingType+="_foundation";
		disableAbilities();
		healthBar.setToGrayScale();		
		health=10;
		healthBar.setMaxHealth(10);
		
		if(getTexture()==null)setFillColor(playerColor);
		else	setFillColor(new Color(55,55,55));
		setOutlineColor(playerColor);
		setOutlineThickness(outlineThickness);
		
		//System.out.println(origin[0]);
		//System.out.println(origin[1]);
		//System.out.println(Main.worldMap.getCell(origin[0],origin[1]).getPosition().x);
			//origin is the cell being hovered
		setPosition(Main.worldMap.getCell(origin[0]-(dimension/2-1)-1, origin[1]-(dimension/2-1)-1).getPosition());
		healthBar.setPosition(getPosition());
		setOrigin(Vector2f.div(getSize(), 2));
		move(Vector2f.div(getSize(), 2));
		move(outlineThickness,outlineThickness);
		for(int[] offset:offsets)
		{
			//Main.worldMap.closeCell(origin[0]+offset[0], origin[1]+offset[1]);
			MapCell c = Main.worldMap.getCell(origin[0]+offset[0], origin[1]+offset[1]);
			occupiedCells.add(c);
			c.close();
			c.registerBuilding(this);
		}
		Main.worldMap.refreshImage();
		allBuildings.put(id,this);

		/*surroundOrder = new SurroundBuildingOrder();
		surroundOrder.init(this);*/
	}
	public SurroundBuildingOrder getSurroundOrder()
	{
		if(surroundOrder==null)
		{
			surroundOrder = new SurroundBuildingOrder();
			surroundOrder.init(this);
		}
		return surroundOrder;
	}
	public void setSpawnLocations()
	{
		for(MapCell c:occupiedCells)
		{
			for(int i=-1;i<=1;i++)
				for(int j=-1;j<=1;j++)
				{
					if(i==0||j==0)//stops corners from being added
					{
						MapCell c2=Main.worldMap.getCell(i+c.x, j+c.y);
						if(c2.isTraversable()&&!spawnLocations.contains(c2))
							spawnLocations.add(c2);
					}
				}			
		}
	}
	public void destroy()
	{
		if(!destroyed)
		{
			destroyed=true;
			allBuildings.remove(id);
			player.getBuildings().remove(this);
			player.getSelectedBuildings().remove(this);
			for(MapCell c:occupiedCells)
			{
				c.open();
				c.deregisterBuilding(this);
			}
			Main.worldMap.refreshImage();
		}
	}
	public void setPlayer(Player p)
	{
		player=p;
		playerColor=p.color;
	}
	@Override
	public void draw(RenderTarget arg0, RenderStates arg1)
	{
		if(valid&&CommonFunctions.isVisible(this,((GameWindow)arg0).activePlayer))
		{
			super.draw(arg0, arg1);
			setOutlineColor(playerColor);
			if(selected)
				setOutlineColor(Color.WHITE);
			if(SHOW_HEALTH_BARS&&healthBarEnabled)
				arg0.draw(healthBar);
		}
	}	
	public void updateHealth(int dmg)
	{
		if(inConstruction)
		{
			dmg=-5;
		}
			health+=dmg;
			health=Math.max(0, health);
			health=Math.min(maxHealth, health);
			
			healthBar.update(dmg);
		
		if(health<=0)
		{
			if(inConstruction)
			{
				completeConstruction();
				//setMaxHealth(maxHealth);
			}
			else
				destroy();
		}
	}
	public void hover()
	{
		if(!selected)setOutlineColor(Color.add(new Color(200,200,200),playerColor));
	}
	public void select()
	{
		selected=true;
	}
	public void deselect()
	{
		selected=false;
	}
	public String getType()
	{
		return buildingType;
	}
	public Vector2f getSpawnLocation()
	{
		return spawnLocations.get((int)(Math.random()*spawnLocations.size())).getCenter();
	}
	@Override
	public boolean contains(Vector2f v)
	{
		if(getGlobalBounds().contains(v))
			return true;
		return false;
	}
	public float getDistance(CircleShape e)
	{
		return (float) (CommonFunctions.getDist(getGlobalBounds(),e.getPosition())-e.getRadius());
	}
	public float getDistance(RectangleShape r)
	{
		return (float) (CommonFunctions.getDist(r.getPosition(), getPosition())-r.getSize().x-getSize().x);
	}
	@Override
	public boolean isActing()
	{
		return acting;
	}
	@Override
	public boolean isAlive()
	{
		return !destroyed;
	}
	@Override
	public Vector2f getTargetPoint(Vector2f v)
	{
		return CommonFunctions.getClosestPoint(getGlobalBounds(), v);
	}
	@Override
	public Player getOwner()
	{
		return player;
	}
}
