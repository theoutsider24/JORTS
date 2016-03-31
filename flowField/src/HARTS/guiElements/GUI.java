package HARTS.guiElements;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

public class GUI extends JFrame{
	IncomingMessagesPanel incomingMessages;
	DesiresPanel desiresPanel;
	BuildingsPanel buildingsPanel;
	ResourcesPanel resourcesPanel;
	public GUI()
	{
		super("HARTS Data");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(1000,1000));
		setLocation(1920, 0);
		
		setLayout(new GridLayout(3,2));
		
		desiresPanel=new DesiresPanel();
		desiresPanel.addToFrame(this);
		
		incomingMessages=new IncomingMessagesPanel();
		incomingMessages.addToFrame(this);
		
		buildingsPanel=new BuildingsPanel();
		buildingsPanel.addToFrame(this);
		
		resourcesPanel=new ResourcesPanel();
		resourcesPanel.addToFrame(this);
		
		pack();
		setVisible(true);
	}
}
