package HARTS.guiElements;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import HARTS.Desires;
import HARTS.Main;

public class DesiresPanel extends JLabel implements Observer{
	public DesiresPanel()
	{
		super();
		setMinimumSize(new Dimension(100, 500));
		
		Main.core.desires.addObserver(this);
		update(Main.core.desires,0);
	}
	public void addToFrame(JFrame j)
	{
		j.getContentPane().add(this);
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		Desires d=(Desires)arg0;
		String text="<html>";
		text+="UNITS<br>";
		for(String s:d.unitDesires.keySet())
		{
			text+=s+": "+d.unitDesires.get(s)+"<br>";
		}
		text+="BUILDING<br>";
		for(String s:d.buildingDesires.keySet())
		{
			text+=s+": "+d.buildingDesires.get(s)+"<br>";
		}
		text+="RESOURCES<br>";
		for(String s:d.resourceDesires.keySet())
		{
			text+=s+": "+d.resourceDesires.get(s)+"<br>";
		}
		text+="</html>";	
		setText(text);
	}
}
