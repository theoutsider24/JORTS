package uiComponents;

import static common.Constants.*;

import java.io.IOException;
import java.util.ArrayList;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.View;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Mouse;

import FYP.GameWindow;
import FYP.Main;
import gameElements.buildings.Building;
import gameElements.buildings.BuildingFactory;
import gameElements.units.UnitFactory;
import test.Logger;
import uiComponents.CommandLineInterface.CommandQueue;
import uiComponents.CommandLineInterface.Console;
import uiComponents.buttons.ExitButton;
import uiComponents.buttons.StandardButton;
import uiComponents.buttons.uiButton;
import uiComponents.grids.ActionButtonGrid;
import uiComponents.grids.ButtonGrid;
import uiComponents.grids.ControlGroupButtonGrid;
import uiComponents.grids.StandardButtonGrid;
import uiComponents.textFields.FPSTimer;
import uiComponents.textFields.GameClock;
import uiComponents.textFields.PlayerLabel;
import uiComponents.textFields.UnitCapCounter;
import uiComponents.textFields.UpdatableTextField;

public class GUI implements Drawable{
	View view;
	GameWindow window;
	public Minimap minimap;
	public ButtonGrid grid1,grid2,grid3;
	//public ArrayList<uiButton> buttons;
	public RectangleShape lowerBackground;
	public RectangleShape topBackground;
	public GameClock clock;
	public FPSTimer fpsTimer;
	public ArrayList<Drawable> drawables;
	public Cursor cursor;
	public ExitButton exit;
	public Console console;
	public PlayerList playerList;
	public SelectionRect selectionRect;
	public PauseOverlay pauseOverlay;
	public UnitCapCounter unitCapCounter;
	UpdatableTextField cursorState;
	public BuildingStatusLayout buildingStatusLayout;
	public ArrayList<RectangleShape> rects = new ArrayList<RectangleShape>();
	public GUI(GameWindow window)
	{
		this.window=window;
		drawables = new ArrayList<Drawable>();
		this.view=window.uiView;
		minimap=new Minimap(window);
		minimap.setPosition(RESOLUTION_X-minimap.getSize().x-20,RESOLUTION_Y-minimap.getSize().y-20);
		//buttons = new ArrayList<uiButton>();
		
		lowerBackground=new GuiRectangle();
		lowerBackground.setPosition(0, RESOLUTION_Y-LOWER_GUI_HEIGHT);
		lowerBackground.setSize(new Vector2f(RESOLUTION_X,LOWER_GUI_HEIGHT));
		lowerBackground.setFillColor(new Color(50,50,50));
		
		topBackground=new GuiRectangle();
		topBackground.setPosition(0, 0);
		topBackground.setSize(new Vector2f(RESOLUTION_X,UPPER_GUI_HEIGHT));
		topBackground.setFillColor(new Color(50,50,50));
		
		buildingStatusLayout = new BuildingStatusLayout(this);
		
		playerList=new PlayerList(window);
		
		console=new Console();
		
		clock = new GameClock();
		fpsTimer = new FPSTimer();
		unitCapCounter = new UnitCapCounter(window);
		
		selectionRect=new SelectionRect(window);
		cursor=new Cursor(window);
		cursor.setColor(Color.RED);

		cursorState = new UpdatableTextField() {
			@Override
			public void update() {
				setText(cursor.state);
			}
		};
		cursorState.setPosition(new Vector2f(10,RESOLUTION_Y-LOWER_GUI_HEIGHT-70));
		
		grid1 = new StandardButtonGrid(1, 7, new Vector2f(10,100));
		grid2 = new ControlGroupButtonGrid(1, 9, new Vector2f(700, RESOLUTION_Y-LOWER_GUI_HEIGHT-40),window);
		grid3= new ActionButtonGrid(0, 0, new Vector2f(20,RESOLUTION_Y-LOWER_GUI_HEIGHT+20),window);
		initButtons();
		
		pauseOverlay=new PauseOverlay();
		
		rects.add(minimap);
		rects.add(grid1);
		rects.add(grid2);
		rects.add(grid3);

		rects.add(buildingStatusLayout.currentSlot);
		rects.add(buildingStatusLayout.slots);
		
		
		rects.add(clock);
		rects.add(fpsTimer);
		rects.add(unitCapCounter);
		rects.add(exit);
		rects.add(lowerBackground);
		rects.add(topBackground);
	}
	public String getMouseHover(Vector2f v)
	{
		for(RectangleShape r:rects)
		{
			if(r.getGlobalBounds().contains(v))
			{
				String result = r.toString();
				
				if(result.contains("buttonGrid"))
				{
					try{result=((ButtonGrid)r).getButton(v).toString();}catch(Exception e){}
				}
				return result;	
			}
		}
		return null;
		
	}
	@Override
	public void draw(RenderTarget window, RenderStates arg1) 
	{
		window.setView(view);
		window.draw(topBackground);
		window.draw(lowerBackground);
		window.draw(grid1);
		((ControlGroupButtonGrid) grid2).update();
		window.draw(grid2);
		((ActionButtonGrid) grid3).update();
		window.draw(grid3);
		if(MINIMAP_ON)
		{
			minimap.update();
			window.draw(minimap);
		}
		
		if(this.window.activePlayer.getSelectedBuildings().size()==1)
		{
			buildingStatusLayout.update(this.window.activePlayer.getSelectedBuildings().get(0));
			window.draw(buildingStatusLayout);
		}
		buildingStatusLayout.progressBar.update();
		
		clock.update();
		window.draw(clock);
		
		fpsTimer.update();
		window.draw(fpsTimer);
		
		unitCapCounter.update();
		window.draw(unitCapCounter);
		
		window.draw(exit);		
		window.draw(playerList);
		
		console.lineTimerTick();
		CommandQueue.tick();
		window.draw(console);
		//cursor.setPosition(new Vector2f(Mouse.getPosition((RenderWindow)window).x,Mouse.getPosition((RenderWindow)window).y));
		//cursor.update();
		window.draw(selectionRect);
		window.draw(cursor);
		cursorState.update();
		window.draw(cursorState);
		if(PAUSED)window.draw(pauseOverlay);
		//for(Drawable d:drawables)
		//	window.draw(d);
	}
	public void initButtons()
	{		
		//grid1 = new StandardButtonGrid(1, 5, new Vector2f(10,100));
		exit = new ExitButton("",window);
		
		
		grid1.addButton(new StandardButton("Zoom In"){public void click(){
			Main.getActiveWindow().gui.console.runCommand("zoom_in 2"); }}, 0, 0);
		
		grid1.addButton(new uiButton("Zoom Out"){public void click(){
			Main.getActiveWindow().gui.console.runCommand("zoom_out 2"); }}, 0, 1);
		
		uiButton b1 = new StandardButton("Toggle Edit Map"){public void click(){
			Main.getActiveWindow().gui.console.runCommand("toggle_edit_map"); }};
			b1.setTogglable(true);
		grid1.addButton(b1, 0, 2);
		
		uiButton b2 = new StandardButton("Toggle Show Flow"){public void click(){
			Main.getActiveWindow().gui.console.runCommand("toggle_show_flow"); }};
			b2.setTogglable(true);
		grid1.addButton(b2, 0, 3);
		
		grid1.addButton(new StandardButton("Print Unit Log"){public void click(){
			try {
				Logger.logUnitPositions();
			} catch (IOException e) {}
		}}, 0,4);
		
		grid1.addButton(new StandardButton("Place unit"){public void click(){
			Main.getActiveWindow().gui.console.runCommand("place_unit cavalry");
			}} , 0, 5);
		
		grid1.addButton(new StandardButton("Place Building"){public void click(){
			Main.getActiveWindow().gui.console.runCommand("place_building");
			}} , 0, 6);
		//grid2 = new ControlGroupButtonGrid(1, 9, new Vector2f(700, RESOLUTION_Y-LOWER_GUI_HEIGHT-40));
		grid2.setOutlineThickness(0);
	}
}
