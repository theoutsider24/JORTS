package HARTS.guiElements;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;

import HARTS.Desires;
import HARTS.Main;
import HARTS.managers.BuildingManager;

public class BuildingsPanel extends JLabel implements Observer{
	public BuildingsPanel()
	{
		super();
		setMinimumSize(new Dimension(100, 500));
		
		Main.core.buildingManager.addObserver(this);
		update(Main.core.buildingManager,0);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		BuildingManager d=(BuildingManager)arg0;
		String text="<html>";
		
		for(String player:d.buildings.keySet())
		{
			text+=player+": "+d.buildings.get(player).size()+" buildings<br>";
		}
		
		text+="</html>";	
		setText(text);
	}

	public void addToFrame(JFrame j)
	{
		j.getContentPane().add(this);
	}
}
