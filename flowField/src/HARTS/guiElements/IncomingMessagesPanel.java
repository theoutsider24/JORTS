package HARTS.guiElements;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import HARTS.Main;

public class IncomingMessagesPanel extends JLabel implements Observer{
	JScrollPane scroller;
	public IncomingMessagesPanel()
	{
		super();
		setMinimumSize(new Dimension(100, 500));
		scroller = new JScrollPane(this, 
			      JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			      JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		Main.core.listener.addObserver(this);
	}
	public void addToFrame(JFrame j)
	{
		j.getContentPane().add(scroller);
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		String currentText=getText();
		currentText=currentText.replaceAll("<html>", "");
		currentText=currentText.replaceAll("</html>", "");
		setText("<html>"+(String)arg1+"<br>"+ currentText+"</html>");	
		
	}
}
