package JORTS.uiComponents.grids;

import java.util.ArrayList;

import org.jsfml.system.Vector2f;

import JORTS.gameElements.units.Entity;
import JORTS.uiComponents.GUI;
import JORTS.uiComponents.buttons.SmallButton;

public class ControlGroupButtonGrid extends SmallButtonGrid {
	public ControlGroupButtonGrid(int x, int y, Vector2f pos, GUI gui) 
	{
		super(10,1, new Vector2f(pos.x-SmallButton.size.x*5,pos.y),gui);
		
		addButton(new SmallButton("1"){public void click(){
			gui.window.activePlayer.selectControlGroup(1); }},0, 0);
		addButton(new SmallButton("2"){public void click(){
			gui.window.activePlayer.selectControlGroup(2); }},1, 0);
		addButton(new SmallButton("3"){public void click(){
			gui.window.activePlayer.selectControlGroup(3); }},2, 0);
		addButton(new SmallButton("4"){public void click(){
			gui.window.activePlayer.selectControlGroup(4); }},3, 0);
		addButton(new SmallButton("5"){public void click(){
			gui.window.activePlayer.selectControlGroup(5); }},4, 0);
		addButton(new SmallButton("6"){public void click(){
			gui.window.activePlayer.selectControlGroup(6); }},5, 0);
		addButton(new SmallButton("7"){public void click(){
			gui.window.activePlayer.selectControlGroup(7); }},6, 0);
		addButton(new SmallButton("8"){public void click(){
			gui.window.activePlayer.selectControlGroup(8); }},7, 0);
		addButton(new SmallButton("9"){public void click(){
			gui.window.activePlayer.selectControlGroup(9); }},8, 0);
		addButton(new SmallButton("0"){public void click(){
			gui.window.activePlayer.selectControlGroup(0); }},9, 0);
	}
	public void update()
	{
		for(int m=0;m<buttons.length;m++)
		{
			for(int n=0;n<buttons[0].length;n++)
				buttons[m][n].disable();
		}
			
		ArrayList<Entity> entities = gui.window.activePlayer.getUnits();
		for(Entity e:entities)
		{
			if(e.getControlGroup()>0)
				buttons[e.getControlGroup()-1][0].enable();
			else if(e.getControlGroup()==0)
				buttons[9][0].enable();
		}
	}
}
