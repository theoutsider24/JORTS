package uiComponents.textFields;

import static common.Constants.*;

import org.jsfml.graphics.Color;
import org.jsfml.system.Clock;
import org.jsfml.system.Vector2f;

import FYP.Main;
import FYP.Player;

public class PlayerLabel extends UpdatableTextField {
	public PlayerLabel(Player p)
	{
		super();
		player=p;
		
		setPosition(RESOLUTION_X-100,RESOLUTION_Y-LOWER_GUI_HEIGHT-50);
		setFillColor(Color.TRANSPARENT);
		setSize(new Vector2f(80,25));
		text.setColor(player.color);
		
		text.setCharacterSize(15);
		//text.move(new Vector2f(3,0));
		text.setOrigin(-3,0);
		text.setString(p.id);
	}
	Player player;
	@Override
	public void update() {
		if(player==Main.activePlayer)
		{
			setFillColor(new Color(255,255,255,130));
		}
		else
			setFillColor(Color.TRANSPARENT);

	}

}
