package JORTS.uiComponents;

import static JORTS.common.Constants.*;

import java.util.ArrayList;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.View;
import org.jsfml.system.Vector2f;

import JORTS.behaviour.timedBehaviours.TimedBehaviour;
import JORTS.core.GameWindow;
import JORTS.uiComponents.CommandLineInterface.CommandQueue;
import JORTS.uiComponents.CommandLineInterface.Console;
import JORTS.uiComponents.buttons.ExitButton;
import JORTS.uiComponents.grids.ActionButtonGrid;
import JORTS.uiComponents.grids.ButtonGrid;
import JORTS.uiComponents.grids.ControlGroupButtonGrid;
import JORTS.uiComponents.textFields.FPSTimer;
import JORTS.uiComponents.textFields.GameClock;
import JORTS.uiComponents.textFields.ResourceList;
import JORTS.uiComponents.textFields.TextField;
import JORTS.uiComponents.textFields.UnitCapCounter;
import JORTS.uiComponents.textFields.UpdatableTextField;

public class GUI implements Drawable{
	View view;
	public GameWindow window;
	public Minimap minimap;
	public ButtonGrid controlGroupButtonGrid,actionButtonGrid;
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
	public UnitStatusLayout unitStatusLayout;
	public ResourceList resourceList;
	public TextField errorMessage;
	public MapEditorGui mapEditorGui;
	public ArrayList<RectangleShape> rects = new ArrayList<RectangleShape>();
	public GUI(GameWindow window)
	{
		this.window=window;
		init();
	}
	public void init()
	{
		errorMessage=new TextField();
		errorMessage.text.setColor(Color.RED);
		errorMessage.text.setCharacterSize(10);
		errorMessage.setFillColor(Color.TRANSPARENT);
		errorMessage.setOutlineColor(Color.TRANSPARENT);
		errorMessage.setPosition((RESOLUTION_X/2)-50, RESOLUTION_Y-LOWER_GUI_HEIGHT-80);
		errorMessage.setSize(new Vector2f(200,15));
		
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
		unitStatusLayout = new UnitStatusLayout(this);
		
		playerList=new PlayerList(window);
		exit = new ExitButton("",window);
		console=new Console();
		
		clock = new GameClock();
		fpsTimer = new FPSTimer();
		
		unitCapCounter = new UnitCapCounter(window);
		resourceList=new ResourceList(window);
		
		selectionRect=new SelectionRect(window);
		cursor=new Cursor(window);
		cursor.setColor(Color.RED);

		mapEditorGui=new MapEditorGui(this);
		mapEditorGui.disable();
		
		cursorState = new UpdatableTextField() {
			@Override
			public void update() {
				setText(cursor.state);
			}
		};
		cursorState.setPosition(new Vector2f(10,RESOLUTION_Y-LOWER_GUI_HEIGHT-70));
		
		//grid1 = new StandardButtonGrid(1, 7, new Vector2f(10,100),this);
		controlGroupButtonGrid = new ControlGroupButtonGrid(1, 9, new Vector2f(RESOLUTION_X/2, RESOLUTION_Y-LOWER_GUI_HEIGHT-40),this);
		actionButtonGrid= new ActionButtonGrid(0, 0, new Vector2f(20,RESOLUTION_Y-LOWER_GUI_HEIGHT+20),this);
		
		//controlGroupButtonGrid.setOutlineThickness(0);
		//initButtons();
		
		pauseOverlay=new PauseOverlay();
		
		rects.add(minimap);
		//rects.add(grid1);
		rects.add(controlGroupButtonGrid);
		rects.add(actionButtonGrid);

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
					result="";
					try{result=((ButtonGrid)r).getButton(v).toString();}catch(Exception e){}
				}
				if(result.equals(""))
					return null;
				return result;	
			}
		}
		return null;
		
	}
	public void setErrorMessage(String s)
	{
		errorMessage.setText(s);
		TimedBehaviour t=new TimedBehaviour(2000) {			
			@Override
			public void run() {
				errorMessage.setText("");
			}
		};
		t.start();
	}
	@Override
	public void draw(RenderTarget window, RenderStates arg1) 
	{
		window.setView(view);
		window.draw(topBackground);
		window.draw(lowerBackground);
		//window.draw(grid1);
		((ControlGroupButtonGrid) controlGroupButtonGrid).update();
		window.draw(controlGroupButtonGrid);
		
		window.draw(errorMessage);
		
		((ActionButtonGrid) actionButtonGrid).update();
		window.draw(actionButtonGrid);
		if(MINIMAP_ON)
		{
			minimap.update();
			window.draw(minimap);
		}

		buildingStatusLayout.update(this.window.activePlayer.getSelectedBuildings());
		if(this.window.activePlayer.getSelectedBuildings().size()>0)
		{
			window.draw(buildingStatusLayout);
		}
		buildingStatusLayout.progressBar.update();
		
		if(this.window.activePlayer.getSelectedUnits().size()>0)
		{
			unitStatusLayout.update(this.window.activePlayer.getSelectedUnits());
			window.draw(unitStatusLayout);
		}
		
		if(EDITING_MAP)window.draw(mapEditorGui);
		
		clock.update();
		window.draw(clock);
		
		fpsTimer.update();
		window.draw(fpsTimer);
		
		unitCapCounter.update();
		window.draw(unitCapCounter);
		
		resourceList.update();
		window.draw(resourceList);
		
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
	/*public void initButtons()
	{		
		//grid1 = new StandardButtonGrid(1, 5, new Vector2f(10,100));
		
		
		
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
	}*/
}
