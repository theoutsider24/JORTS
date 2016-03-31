package HARTS;

import java.io.IOException;

import HARTS.guiElements.GUI;
import HARTS.managers.BuildingManager;
import HARTS.managers.ProductionManager;
import HARTS.managers.QueryManager;
import HARTS.managers.UnitManager;
import JORTS.communication.Client;

public class Main extends Client{
	public static Main core;
	public EventListener listener;
	public GUI gui;
	public Desires desires;
	public String playerName="HARTS";

	public UnitManager unitManager;
	public BuildingManager buildingManager;
	
	public QueryManager queryManager;
	public ProductionManager productionManager;
	public Main() throws IOException
	{
		super("HARTS");
		listener=new EventListener(); 
		setEventListener(listener);
	}
	public static void main(String[] args) throws IOException
	{
		core=new Main();
		core.unitManager=new UnitManager(core.listener);
		core.buildingManager=new BuildingManager(core.listener);
		core.queryManager=new QueryManager(core.listener);
		core.start();
		while(!core.ready&&core.isAlive()){System.out.print("");}
		if(core.isAlive())
		{
			String players=core.queryManager.getPlayerList();
			core.unitManager.addPlayers(players);
			core.buildingManager.addPlayers(players);
			core.desires=new Desires();
			core.desires.increaseDesire("villager", 100);
			core.productionManager=new ProductionManager(core.listener);
	
	
			core.gui=new GUI();
		}
		
		while(core.isAlive());
		core.gui.dispose();
	}
	
}
