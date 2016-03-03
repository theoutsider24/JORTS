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

import FYP.Main;
import buildings.Barracks;
import buildings.Building;
import test.Logger;
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
import uiComponents.textFields.UpdatableTextField;
import units.Infantry;

public class GUI implements Drawable{
	View view;
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
	UpdatableTextField cursorState;
	ArrayList<RectangleShape> rects = new ArrayList<RectangleShape>();
	public GUI(View view)
	{
		drawables = new ArrayList<Drawable>();
		this.view=view;
		minimap=new Minimap();
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
		
		playerList=new PlayerList();
		
		console=new Console();
		
		clock = new GameClock();
		fpsTimer = new FPSTimer();
		
		selectionRect=new SelectionRect();
		cursor=new Cursor();
		cursor.setColor(Color.RED);

		cursorState = new UpdatableTextField() {
			@Override
			public void update() {
				setText(cursor.state);
			}
		};
		cursorState.setPosition(new Vector2f(10,RESOLUTION_Y-LOWER_GUI_HEIGHT-70));
		
		grid1 = new StandardButtonGrid(1, 7, new Vector2f(10,100));
		grid2 = new ControlGroupButtonGrid(1, 9, new Vector2f(700, RESOLUTION_Y-LOWER_GUI_HEIGHT-40));
		grid3= new ActionButtonGrid(0, 0, new Vector2f(20,RESOLUTION_Y-LOWER_GUI_HEIGHT+20));
		initButtons();
		
		pauseOverlay=new PauseOverlay();
		
		rects.add(minimap);
		rects.add(grid1);
		rects.add(grid2);
		rects.add(grid3);
		rects.add(clock);
		rects.add(fpsTimer);
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
					result=((ButtonGrid)r).getButtonTitle(v);
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
		minimap.update();
		window.draw(minimap);
		clock.update();
		window.draw(clock);
		fpsTimer.update();
		window.draw(fpsTimer);
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
		exit = new ExitButton("");
		
		
		grid1.addButton(new StandardButton("Zoom In"){public void click(){
			Main.zoom(ZOOM_VALUE); }}, 0, 0);
		grid1.addButton(new uiButton("Zoom Out"){public void click(){
			Main.zoom(1/ZOOM_VALUE); }}, 0, 1);
		
		uiButton b1 = new StandardButton("Toggle Edit Map"){public void click(){
			Main.editMapMode=!Main.editMapMode; }};
			b1.setTogglable(true);
		grid1.addButton(b1, 0, 2);
		
		uiButton b2 = new StandardButton("Toggle Show Flow"){public void click(){
			Main.showFlow=!Main.showFlow; }};
			b2.setTogglable(true);
		grid1.addButton(b2, 0, 3);
		
		grid1.addButton(new StandardButton("Print Unit Log"){public void click(){
			try {
				Logger.logUnitPositions();
			} catch (IOException e) {}
		}}, 0,4);
		
		grid1.addButton(new StandardButton("Place unit"){public void click(){
			if(cursor.attachedUnit==null)
			{
				cursor.attachedUnit=new Infantry(); 
				cursor.attachedUnit.disable();
				Main.activePlayer.addUnit(cursor.attachedUnit);
			}
			else
				cursor.attachedUnit=null;
			}} , 0, 5);
		
		grid1.addButton(new StandardButton("Place Building"){public void click(){
			if(cursor.attachedBuilding==null)
			{
				cursor.attachedBuilding=new Barracks(); 
				Main.activePlayer.addBuilding(cursor.attachedBuilding);
			}
			else
				cursor.attachedBuilding=null;
			}} , 0, 6);
		//grid2 = new ControlGroupButtonGrid(1, 9, new Vector2f(700, RESOLUTION_Y-LOWER_GUI_HEIGHT-40));
		grid2.setOutlineThickness(0);
	}
}
