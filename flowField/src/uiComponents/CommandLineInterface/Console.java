package uiComponents.CommandLineInterface;

import java.util.ArrayList;
import java.util.Arrays;

import FYP.Main;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;

public class Console implements Drawable{
	ArrayList<Text> history;
	
	public boolean isOpen;
	public static Keyboard.Key activationKey = Keyboard.Key.HOME;
	
	Text currentTextDisplay;
	String currentText="";
	
	RectangleShape entryBox;
	RectangleShape historyBox;
	
	int upSelectIndex;
	
	int consoleWidth=250*2;
	
	final int historyLimit=34;
	
	int outlineThickness=2;
	
	int timer;
	
	
	
	public Console()
	{
		history = new ArrayList<Text>();		
		
		entryBox = new RectangleShape();
		entryBox.setSize(new Vector2f(consoleWidth,25
				));
		entryBox.setFillColor(new Color(0,0,0,135));
		entryBox.setOutlineColor(new Color(200,200,200));
		entryBox.setOutlineThickness(outlineThickness);
		
		historyBox = new RectangleShape();
		historyBox.move(0,25);
		historyBox.setFillColor(new Color(0,0,0,100));
		historyBox.setOutlineColor(new Color(200,200,200));
		historyBox.setOutlineThickness(outlineThickness);
		
		currentTextDisplay = new Text(">|", Main.font, 15);
		
		//this.move(-1280,-720);		
	}

	/*@Override
	public void draw(RenderWindow window) {
		if(isOpen)
		{
			window.draw(entryBox);
			window.draw(historyBox);	
			window.draw(currentTextDisplay);
			for(Text t:history)
				window.draw(t);
		}
	}*/
	@Override
	public void draw(RenderTarget window, RenderStates arg1) {
		if(isOpen)
		{
			window.draw(entryBox);
			window.draw(historyBox);	
			window.draw(currentTextDisplay);
			for(Text t:history)
				window.draw(t);
		}
	}
	public void runCommand(String s)
	{
		//System.out.println(s);
		String[] cmds=s.split(";");
		for(String cmd:cmds)
		{
			addCharacter(cmd);
			submitText(false);
		}
	}
	public void addCharacter(String s)
	{
		currentText+=s;
		currentTextDisplay.setString(">"+currentText+ "|");
	}
	public void lineTimerTick()
	{
		timer++;
		if(timer>30)
		{
			timer=0;
			toggleLine();
		}
	}
	public void toggleLine()
	{
		String s =currentTextDisplay.getString();
		if(currentTextDisplay.getString().charAt(s.length()-1) == '|')
			currentTextDisplay.setString(s.substring(0, s.length()-1));
		else
			currentTextDisplay.setString(s+"|");
	}
	public void move(int x,int y)
	{
		entryBox.move(x,y);
		historyBox.move(x,y+outlineThickness);
		currentTextDisplay.move(x,y-outlineThickness);
	}
	public void open()
	{
		isOpen=true;
	}
	public void close()
	{
		isOpen=false;
	}
	public void submitText()
	{
		submitText(true);
	}
	public void submitText(boolean addToHistory)
	{
		currentText=currentText.trim();
		
		String textStore=currentText;
		resetText();
		
		if(textStore.length()==0)
			return;
		String command = textStore.split(" ")[0];
		
		String [] args = Arrays.copyOfRange(textStore.split(" "), 1, textStore.split(" ").length);
		boolean succesful =Commands.submitCommand(command,args);
		
		
		if(addToHistory)
		{
			for(Text t:history)
				t.move(0,20);
			
				
			Text temp = new Text(textStore, Main.font, 15);
			temp.move(new Vector2f(0,entryBox.getSize().y));
			//temp.move(-1280,-670);
			if(!succesful&&!textStore.equals(""))
			{
				temp.setString(textStore +" -- INVALID COMMAND");
				temp.setColor(Color.RED);
			}
			
			history.add(temp);
			
			if(history.size()>historyLimit)
				history.remove(0);
			historyBox.setSize(new Vector2f(consoleWidth,20*history.size()));
			
			
			upSelectIndex=history.size();
		}
	}
		
	public void backspace()
	{
		if(currentText.length()>0)
		{
			currentText=currentText.substring(0,currentText.length()-1);
			currentTextDisplay.setString(">"+currentText+ "|");
		}
	}

	public void loadPreviousEntry() {
		if(upSelectIndex>0)
		{
			upSelectIndex--;
			resetText();
			addCharacter(history.get(upSelectIndex).getString().replaceAll(" -- INVALID COMMAND", ""));
				
		}
		//System.out.println(upSelectIndex);
	}
	public void loadNextEntry() {
		if(upSelectIndex<history.size()-1)
		{
			upSelectIndex++;
			resetText();
			addCharacter(history.get(upSelectIndex).getString().replaceAll(" -- INVALID COMMAND", ""));
				
		}
		//System.out.println(upSelectIndex);
	}
	public void resetText()
	{
		currentText="";
		addCharacter("");
	}
	public void clearHistory()
	{
		history.clear();
		historyBox.setSize(new Vector2f(consoleWidth,0));
	}

	
}
