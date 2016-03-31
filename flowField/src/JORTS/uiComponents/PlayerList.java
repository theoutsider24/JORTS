package JORTS.uiComponents;

import java.util.ArrayList;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;

import JORTS.core.GameWindow;
import JORTS.core.Main;
import JORTS.core.Player;
import JORTS.uiComponents.textFields.PlayerLabel;

public class PlayerList implements Drawable{
	ArrayList<PlayerLabel> labels;
	GameWindow window;
	public PlayerList(GameWindow window)
	{
		this.window=window;
		labels=new ArrayList<PlayerLabel>();
		for(Player p:Main.players)
		{
			for(PlayerLabel l:labels)
			{
				l.move(new Vector2f(0,-30));
			}		
			labels.add(new PlayerLabel(p,window));
		}
	}
	public void update()
	{
		for(PlayerLabel l:labels)
		{
			l.update();
		}
	}
	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) {
		for(PlayerLabel l:labels)
		{
			arg0.draw(l);
		}
	}
}
