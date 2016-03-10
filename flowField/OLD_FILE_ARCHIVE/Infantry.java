package units;

import FYP.Ability;
import FYP.Main;
import buildings.Barracks;
import buildings.Building;

public class Infantry extends Entity{
	public Infantry(int x,int y)
	{
		super(x,y);
		setUnitType("infantry");
		
		maxSpeed=2;
		setMaxHealth(100);
		setRadius(15);
		
		abilities.add(new Ability("Place Barracks"){public void run(){
			if(Main.gui.cursor.attachedBuilding==null)
			{
				Main.gui.cursor.attachedBuilding=new Barracks(); 
				Main.activePlayer.addBuilding(Main.gui.cursor.attachedBuilding);
			}
			else
				Main.gui.cursor.attachedBuilding=null;
			}});;
	}
	public Infantry()
	{
		this(0,0);
	}
}
