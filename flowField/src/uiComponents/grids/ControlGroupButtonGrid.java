package uiComponents.grids;

import static common.Constants.ZOOM_VALUE;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.jsfml.system.Vector2f;

import FYP.GameWindow;
import FYP.Main;
import gameElements.units.Entity;
import uiComponents.buttons.SmallButton;

public class ControlGroupButtonGrid extends SmallButtonGrid {
	GameWindow window;
	public ControlGroupButtonGrid(int x, int y, Vector2f pos, GameWindow window) {
		super(10,1, pos);
		//int i=0;
		this.window=window;
		
		addButton(new SmallButton("1"){public void click(){
			window.activePlayer.selectControlGroup(1); }},0, 0);
		addButton(new SmallButton("2"){public void click(){
			window.activePlayer.selectControlGroup(2); }},1, 0);
		addButton(new SmallButton("3"){public void click(){
			window.activePlayer.selectControlGroup(3); }},2, 0);
		addButton(new SmallButton("4"){public void click(){
			window.activePlayer.selectControlGroup(4); }},3, 0);
		addButton(new SmallButton("5"){public void click(){
			window.activePlayer.selectControlGroup(5); }},4, 0);
		addButton(new SmallButton("6"){public void click(){
			window.activePlayer.selectControlGroup(6); }},5, 0);
		addButton(new SmallButton("7"){public void click(){
			window.activePlayer.selectControlGroup(7); }},6, 0);
		addButton(new SmallButton("8"){public void click(){
			window.activePlayer.selectControlGroup(8); }},7, 0);
		addButton(new SmallButton("9"){public void click(){
			window.activePlayer.selectControlGroup(9); }},8, 0);
		addButton(new SmallButton("0"){public void click(){
			window.activePlayer.selectControlGroup(0); }},9, 0);
	}
	public void update()
	{
		for(int m=0;m<buttons.length;m++)
		{
			for(int n=0;n<buttons[0].length;n++)
				buttons[m][n].disable();
		}
			
		ArrayList<Entity> entities = window.activePlayer.getUnits();
		for(Entity e:entities)
		{
			if(e.getControlGroup()>0)
				buttons[e.getControlGroup()-1][0].enable();
			else if(e.getControlGroup()==0)
				buttons[9][0].enable();
		}
	}
}
