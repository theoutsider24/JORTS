package HARTS.guiElements;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;

import HARTS.Main;
import HARTS.managers.BuildingManager;
import HARTS.managers.ProductionManager;

public class ResourcesPanel  extends JLabel implements Observer{
	public ResourcesPanel()
	{
		super();
		setMinimumSize(new Dimension(100, 500));
		
		Main.core.productionManager.addObserver(this);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		ProductionManager d=(ProductionManager)arg0;
		String text="<html>";
		
		for(String res:d.resources.keySet())
		{
			text+=res+": "+d.resources.get(res)+"<br>";
		}
		
		text+="</html>";	
		setText(text);
	}

	public void addToFrame(JFrame j)
	{
		j.getContentPane().add(this);
	}
}

