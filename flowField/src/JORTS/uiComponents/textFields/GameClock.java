package JORTS.uiComponents.textFields;

import static JORTS.common.Constants.RESOLUTION_X;
import static JORTS.common.Constants.UPPER_GUI_HEIGHT;

import org.jsfml.system.Clock;
import org.jsfml.system.Vector2f;

import JORTS.core.Main;

public class GameClock extends UpdatableTextField
{
	public Clock clock;
	int width=100;
	//long milliseconds=0;
	public GameClock()
	{
		super();
		setSize(new Vector2f(width,UPPER_GUI_HEIGHT));
		setOrigin(new Vector2f(width/2,0));
		text.setOrigin(new Vector2f(0,0));
		setPosition(RESOLUTION_X/2,0);
		
		text.setCharacterSize(20);
		text.setPosition((RESOLUTION_X/2)-30,0);
		
		clock=new Clock();
		clock.restart();
	}
	@Override
	public void update() {
	/*	if(!PAUSED)		
			milliseconds+=clock.getElapsedTime().asMilliseconds();
			
		clock.restart();*/
		
		long seconds=Main.milliseconds/1000;		
		//String minutes = ""+(int)(clock.getElapsedTime().asSeconds()/60);
		String minutes = ""+(int)(seconds/60);
		if(minutes.length()<2)minutes="0"+minutes;
		//String seconds = ""+(int)(clock.getElapsedTime().asSeconds()%60);
		String secondsS = ""+(int)(seconds%60);
		if(secondsS.length()<2)secondsS="0"+secondsS;
		text.setString(minutes+":"+secondsS);
	}

	@Override
	public String toString()
	{
		return "clock";
	}
}