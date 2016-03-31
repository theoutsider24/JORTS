package JORTS.uiComponents.textFields;

import static JORTS.common.Constants.LOWER_GUI_HEIGHT;
import static JORTS.common.Constants.RESOLUTION_X;
import static JORTS.common.Constants.RESOLUTION_Y;

import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;

import JORTS.core.GameWindow;
import JORTS.core.Player;

public class PlayerLabel extends UpdatableTextField {
	GameWindow window;
	public PlayerLabel(Player p,GameWindow window)
	{
		super();
		this.window=window;
		player=p;
		
		setPosition(RESOLUTION_X-100,RESOLUTION_Y-LOWER_GUI_HEIGHT-50);
		setFillColor(Color.TRANSPARENT);
		setSize(new Vector2f(80,25));
		text.setColor(player.color);
		
		text.setCharacterSize(15);
		//text.move(new Vector2f(3,0));
		text.setOrigin(-3,0);
		text.setString(p.name);
	}
	Player player;
	@Override
	public void update() {
		if(player==window.activePlayer)
		{
			setFillColor(new Color(255,255,255,200));
		}
		else
			setFillColor(new Color(255,255,255,100));

	}

}
