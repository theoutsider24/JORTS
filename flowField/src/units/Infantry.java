package units;

import FYP.Ability;
import FYP.Main;
import buildings.Building;

public class Infantry extends Entity{
	public Infantry(int x,int y)
	{
		super(x,y);
		unitType="infantry";
		id=id.replace("unit", "unit_"+unitType);
		maxSpeed=2;
		setMaxHealth(100);
		unitType="infantry";
		setRadius(15);
		
		abilities.add(new Ability("Place Building Ability"){public void run(){
			if(Main.gui.cursor.attachedBuilding==null)
			{
				Main.gui.cursor.attachedBuilding=new Building(); 
				Main.activePlayer.addBuilding(Main.gui.cursor.attachedBuilding);
			}
			else
				Main.gui.cursor.attachedBuilding=null;
			}});
	}
	public Infantry()
	{
		this(0,0);
	}
}
