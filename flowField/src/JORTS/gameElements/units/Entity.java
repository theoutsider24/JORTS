package JORTS.gameElements.units;

import static JORTS.common.Constants.CELL_SIZE;
import static JORTS.common.Constants.GRID_SIZE;
import static JORTS.common.Constants.SHOW_HEALTH_BARS;
import static JORTS.core.Main.worldMap;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.graphics.CircleShape;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import JORTS.behaviour.Attackable;
import JORTS.behaviour.abilities.Ability;
import JORTS.behaviour.flowField.FlowCell;
import JORTS.behaviour.interactions.HarvestInteraction;
import JORTS.behaviour.interactions.RangedUnitCombatInteraction;
import JORTS.behaviour.interactions.UnitAttackCombatInteraction;
import JORTS.behaviour.orders.FollowOrder;
import JORTS.behaviour.orders.Order;
import JORTS.behaviour.timedBehaviours.TimedBehaviour;
import JORTS.common.CommonFunctions;
import JORTS.common.ResourceAmount;
import JORTS.core.GameWindow;
import JORTS.core.Main;
import JORTS.core.Player;
import JORTS.gameElements.Animation;
import JORTS.gameElements.buildings.resources.Resource;
import JORTS.gameElements.projectiles.ProjectileFactory;
import JORTS.uiComponents.HealthBar;

public abstract class Entity extends CircleShape implements Attackable{
	public static HashMap<String,Entity> allEntities = new HashMap<String,Entity>();
	public static int numberOfEntities;
	public String id;
	public int damage;
	public ArrayList<Ability> abilities = new ArrayList<Ability>();
	public String projectileType;
	public Order currentOrder;
	public int visionRange;
	public FollowOrder followOrder=null;
	
	public boolean canConstruct=false;
	
	public boolean dead=false;
	public String unitType;
	Vector2f speed;
	public float maxSpeed;
	int radius=7;
	
	Vector2i currentCell;
	Vector2f repulsion;
	public int influenceRange=10;
	float unitRepulsionResistance=10;
	//Field fieldMap;//=new Field();
	int controlGroup=-1;
	Text controlGroupText;
	Color playerColor=new Color(50,50,50);
	public Player player;
	HealthBar healthBar;
	public int health;
	boolean hovered=false,selected=false;
	public int maxHealth;
	public boolean acting=false;
	boolean enabled=true;
	

	//Combat
	public int attackRange=10;
	public boolean ranged=true;
	public boolean projectileLoaded=true;
	
	//Resource Gathering
	boolean canHarvest=true;	
	public int maxResourceCapacity=20;
	public ResourceAmount heldResources;
	

	
	/*SoundBuffer attackSoundBuffer;
	public Sound attackSound;*/
	
	public Entity(Vector2f v)
	{
		this((int)v.x,(int)v.y);
	}
	public Entity()
	{
		this(0,0);
	}
	public void setUnitType(String unitType)
	{
		this.unitType=unitType;
		allEntities.remove(id, this);
		id=id.replace("unit", "unit_"+unitType);
		allEntities.put(id, this);
	}
	public Entity(int x,int y)
	{
		super();
		
		id="unit_#"+numberOfEntities++;
		
		controlGroupText=new Text("",Main.font);
		controlGroupText.setCharacterSize(8);
		controlGroupText.setColor(Color.BLACK);
		controlGroupText.setOrigin(0, 8);
		
		currentCell = new Vector2i(-1,-1);
		//setOrigin(new Vector2f(radius,radius));
		
		setRadius(radius);
		setFillColor(playerColor);
		
		setOutlineThickness(1);
		setOutlineColor(playerColor);
		
		healthBar=new HealthBar(this);
		
		speed = new Vector2f(0,0);
		repulsion  = new Vector2f(0,0);
		
		reregister();
		setPosition(x,y);
		allEntities.put(id,this);
		Order.IdleOrder.issue(this);
		//currentOrder=Order.IdleOrder;
		
		/*attackSoundBuffer=new SoundBuffer();
		try {
		    attackSoundBuffer.loadFromFile(Paths.get("sounds//arrow.wav"));
		    } catch(IOException ex) {
		    System.err.println("Failed to load the sound:");
		    ex.printStackTrace();
		}
		attackSound=new Sound(attackSoundBuffer);*/
		
	}
	public void kill()
	{
		if(!dead)
		{
			dead=true;
			allEntities.remove(id);
			player.getUnits().remove(this);
			player.getSelectedUnits().remove(this);
			Order.IdleOrder.issue(this);
			Order.IdleOrder.remove(this);
			
			
			int currentX = (int) (getPosition().x/CELL_SIZE);
			int currentY = (int) (getPosition().y/CELL_SIZE);
			for(int i=currentCell.x-1;i<=currentCell.x+1;i++)
			{
				for(int j=currentCell.y-1;j<=currentCell.y+1;j++)
					worldMap.getCell(i,j).deregisterUnit(this);
			}
			player.unitCount--;
			Animation.createDeadBody(getPosition(), new Vector2f(getRadius()*2,getRadius()*2));
		}
	}
	public void disable()
	{
		enabled=false;
	}
	public void enable()
	{
		enabled=true;
	}
	public void hover()
	{
		if(!selected)
		{
			setOutlineColor(new Color(200,200,200));
			hovered=true;
		}
	}
	public void setMaxHealth(int max)
	{
		maxHealth=max;
		health=max;
		healthBar.setMaxHealth(maxHealth);
	}
	public void setPlayer(Player p)
	{
		player=p;
		playerColor=p.color;
		if(getTexture()==null)setFillColor(playerColor);
		else	setFillColor(Color.WHITE);
		setOutlineColor(playerColor);
	}
	public void setRadius(int radius)
	{
		this.radius=radius;
		setOrigin(new Vector2f(radius,radius));
		super.setRadius(radius);
	}
	@Override
	public void setPosition(Vector2f v)
	{
		super.setPosition(v);
		controlGroupText.setPosition(Vector2f.add(v,new Vector2f(radius,-radius)));
		healthBar.setPosition(v);
	}
	public void select()
	{
		setOutlineColor(Color.WHITE);
		selected=true;
	}
	public void deselect()
	{
		setOutlineColor(playerColor);
		selected=false;
	}
	public void updateHealth(int dmg)
	{
		health+=dmg;
		health=Math.max(0, health);
		health=Math.min(maxHealth, health);
		
		healthBar.update(dmg);
		//System.out.println(health);
		if(health<=0)
			kill();
	}
	public void tick()
	{	
		if(!enabled) return;
		if(currentOrder!=null)
		{
			//Vector2f v = currentOrder.flowField.getFlowAtPos(this.getPosition());
			Vector2f v = currentOrder.getVector(this);
			v = Vector2f.mul(v, maxSpeed);
			//enforceBounds();
			repulsion =repel();
			int ratio=10;
			float avgX = (v.x + speed.x*ratio)/(ratio+1);
			float avgY = (v.y + speed.y*ratio)/(ratio+1);
			speed = new Vector2f(avgX,avgY);
			//speed = Vector2f.add(speed,repulsion);
		//	System.out.println(CommonFunctions.getLength(speed));
			//System.out.printf("%f\n",CommonFunctions.getLength(speed));
			moveAndRegister( Vector2f.add(speed,repulsion));
		}
	}
	public void draw(RenderTarget arg0, RenderStates arg1) 
	{
		if(CommonFunctions.isOnScreen(arg0,this)&&CommonFunctions.isVisible(this, ((GameWindow)arg0).activePlayer))
		{
			super.draw(arg0, arg1);
			if(controlGroup!=-1)
			{
				if(((GameWindow)arg0).activePlayer==player) arg0.draw(controlGroupText);
			}
			
			if(hovered&&!selected)
			{
				hovered=false;
				setOutlineColor(playerColor);
			}
	
			if(SHOW_HEALTH_BARS)
				arg0.draw(healthBar);
		}
	}
	private void enforceBounds() {
		this.move(Math.min(CELL_SIZE*GRID_SIZE - this.getPosition().x -getRadius(),0)    ,  Math.min(CELL_SIZE*GRID_SIZE - this.getPosition().y -getRadius(),0));
		
		this.move(Math.max(-this.getPosition().x +getRadius(),0)    ,  Math.max(-this.getPosition().y+getRadius(),0));
	}
	public void reregister()
	{//TODO register in multiple cells
		int currentX = (int) (getPosition().x/CELL_SIZE);
		int currentY = (int) (getPosition().y/CELL_SIZE);
		if(currentX!=currentCell.x || currentY!=currentCell.y)
		{
			for(int i=currentCell.x-1;i<=currentCell.x+1;i++)
			{
				for(int j=currentCell.y-1;j<=currentCell.y+1;j++)
					worldMap.getCell(i,j).deregisterUnit(this);
			}
			for(int i=currentX-1;i<=currentX+1;i++)
			{
				for(int j=currentY-1;j<=currentY+1;j++)
					worldMap.getCell(i,j).registerUnit(this);
			}
			/*worldMap.getCell(currentCell.x, currentCell.y).deregisterUnit(this);
			worldMap.getCell(currentX,currentY).registerUnit(this);*/
			currentCell = new Vector2i(currentX,currentY);
		}
	}
	public Vector2f getUnitRepulsion(Entity e)
	{
		float cosVal = 0;
		float repulsionFactor = 1;
		float distance = (float)CommonFunctions.getDist(this.getPosition(), e.getPosition());
		distance-=this.getRadius();
		distance-=e.getRadius();
		distance = Math.max(distance,0);
		
		if (distance < e.influenceRange)
		{
			cosVal = (distance / e.influenceRange) * 90;
			repulsionFactor = (float) Math.cos(cosVal* 3.14159265 / 180.0)*5;
			Vector2f tempVect = CommonFunctions.normaliseVector(Vector2f.sub(getPosition(),e.getPosition()));
			if(CommonFunctions.getLength(tempVect)==0)
			{
				tempVect = Vector2f.add(tempVect, new Vector2f(1,0));
			}
			tempVect=Vector2f.mul(tempVect, repulsionFactor*2);
			Vector2f result=Vector2f.div(tempVect,unitRepulsionResistance);
			return result;
		}
		return new Vector2f(0,0);
	}
	public Vector2f getCellRepulsion(FlowCell c)
	{
		float cosVal = 0;
		float repulsionFactor = 1f;
		//float distance = (float)CommonFunctions.getDist(this.getPosition(), c.getCenter());
		float distance = (float)CommonFunctions.getDist(c.getGlobalBounds(),this.getPosition());
		distance-=this.getRadius();		
		distance = Math.max(distance,0.1f);
		int maxDist=5;
		
		if (distance < maxDist)
		{					
			cosVal = (distance / maxDist) * 90;//the 5 needs to be the maximum distance at that 
			repulsionFactor = (float) Math.cos(cosVal* 3.14159265 / 180.0)*5;
			Vector2f tempVect = CommonFunctions.normaliseVector(Vector2f.sub(getPosition(),c.getCenter()));
			if(CommonFunctions.getLength(tempVect)==0)
			{
				tempVect = Vector2f.add(tempVect, new Vector2f(1,0));
			}
			tempVect=Vector2f.mul(tempVect, repulsionFactor*2);
			Vector2f result=Vector2f.div(tempVect,unitRepulsionResistance);
			return result;
		}
		return new Vector2f(0,0);
	}
	public Vector2f repel()
	{
		repulsion = new Vector2f(0,0);
		int range=1;
		for(int i=currentCell.x-range;i<=currentCell.x+range;i++)
		{
			for(int j=currentCell.y-range;j<=currentCell.y+range;j++)
			{				
				//for(Entity e: Main.fieldMap.getCell(currentCell.x, currentCell.y).entities)
				if(!currentOrder.flowField.cellExists(i, j))//||!this.fieldMap.getCell(i, j).isOpen())
					continue;
				FlowCell c = currentOrder.flowField.getCell(i, j);
				if(!c.isOpen())
				{
					//if(!(currentOrder.orderType.equals("attackBuilding")&&Main.worldMap.getCell(c.x, c.y).getBuildings().contains(((AttackBuildingOrder)currentOrder).b)))
					//{
						repulsion = Vector2f.add(repulsion,getCellRepulsion(c));
						continue;
					//}					
				}
				ArrayList<Entity> entities = worldMap.getCell(i, j).getEntities();
				for(Entity e: entities)
				{
					if(e!=this)
					{
						repulsion = Vector2f.add(repulsion,getUnitRepulsion(e));				
					}
				}
			}
		}
		//moveAndRegister(repulsion);
		return repulsion;
	}
	
	public void moveAndRegister(float x,float y)
	{
		Vector2f v = new Vector2f(x,y);
		moveAndRegister(v);
	}
	public void moveAndRegister(Vector2f v)
	{
		v= CommonFunctions.limitVectorLength(v, maxSpeed);
		moveAndRegisterAbsolute(v);
	}
	public void moveAndRegisterAbsolute(Vector2f v)
	{
				//Vector2f movement = Vector2f.mul(v,(((float)Main.game.deltaT)/TARGET_DELTA_T));
		Vector2f movement=v;
		
		//if(Math.abs(movement.x)>0.1&&Math.abs(movement.x)>0.1)
			this.move(movement);
			//if(CommonFunctions.getLength(movement)>maxSpeed)System.out.println(maxSpeed+","+CommonFunctions.getLength(movement));
		//	this.move(v);
		//System.out.println(v.toString());
		enforceBounds();
		reregister();
		if(!worldMap.getCell(currentCell.x,currentCell.y).isTraversable())
		{
			//moveAndRegisterAbsolute(Vector2f.add(getCellRepulsion(fieldMap.getCell(currentCell.x,currentCell.y)),Vector2f.mul(v, -1)));
				//Vector2f temp = Vector2f.add(getCellRepulsion(currentOrder.flowField.getCell(currentCell.x,currentCell.y)),Vector2f.mul(v, -1));
			Vector2f temp = Vector2f.add(getCellRepulsion(currentOrder.flowField.getCell(currentCell.x,currentCell.y)),Vector2f.mul(v, -1));
			this.move(temp);//Vector2f.mul(temp,(Main.game.deltaT/7)));
			//moveAndRegisterAbsolute(Vector2f.mul(v, -1));
		}
	}
	public String toString()
	{
		String result="";
		result+="pos:("+ getPosition().x + "," +getPosition().y+")";
		
		return result;
	}
	public String getType()
	{
		return unitType;
	}
	public int getControlGroup()
	{
		return controlGroup;
	}
	public void setControlGroup(int group)
	{
		controlGroup=group;
		if(group==-1)
			controlGroupText.setString("");
		else
			controlGroupText.setString(""+controlGroup);
	}
	@Override
	public boolean contains(Vector2f v)
	{
		if(CommonFunctions.getDist(v, getPosition())<getRadius())
			return true;
		return false;
	}
	public float getDistance(CircleShape e)
	{
		return (float) (CommonFunctions.getDist(getPosition(), e.getPosition())-e.getRadius()-getRadius());
	}
	public float getDistance(RectangleShape r)
	{
		return (float) (CommonFunctions.getDist(r.getGlobalBounds(),getPosition())-getRadius());
	}
	public void fireProjectile(Attackable target)
	{
		if(projectileLoaded)
		{
			try {
				ProjectileFactory.factories.get(projectileType).buildProjectile(this,target,damage,3);
				projectileLoaded=false;
				reload();
			} catch (Exception e) {
				e.printStackTrace();}
		}
	}
	public void reload()
	{
		TimedBehaviour reload = new TimedBehaviour(1000) {
			@Override
			public void run() {
				projectileLoaded=true;
			}
		};
		reload.start();
	}
	@Override
	public boolean isActing()
	{
		return acting;
	}
	@Override
	public boolean isAlive()
	{
		return !dead;
	}
	public void attack(Attackable target)
	{
		if(!acting)
		{
			if(ranged)
			{
			//	attackSound.play();
				new RangedUnitCombatInteraction(500,this,target);
			}
			else			
				new UnitAttackCombatInteraction(1000,this,target);
		}
	}
	@Override
	public Vector2f getTargetPoint(Vector2f v)
	{
		return getPosition();
	}
	@Override
	public Player getOwner()
	{
		return player;
	}
	public void beginHarvest(Resource b)
	{
		if(!acting&&(heldResources==null||heldResources.amount<maxResourceCapacity))
		{
			new HarvestInteraction(1000,this,b);
		}
	}
	public void harvest(Resource res)
	{
		if(heldResources==null)
		{
			heldResources=new ResourceAmount(res.buildingType, 1);
		}
		else if(heldResources.resource.equals(res.buildingType))
		{
			heldResources.amount++;
		}
		else
			heldResources=new ResourceAmount(res.buildingType, 1);
		
		if(heldResources.amount>maxResourceCapacity)
			heldResources.amount=maxResourceCapacity;
		System.out.println(heldResources.resource+","+heldResources.amount);
			
	}
}
