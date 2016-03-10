package uiComponents.buttons;

import static common.Constants.*;
import org.jsfml.graphics.Color;

import FYP.GameWindow;
import FYP.Main;

public class ExitButton extends SmallButton{
	GameWindow window;
	public ExitButton(String title,GameWindow window) {
		super("X");
		this.window=window;
		text.setCharacterSize(30);
		text.setOrigin(-2,10);
		setFillColor(Color.RED);
		setPosition(RESOLUTION_X-30, 5);
	}
	@Override
	public void click()
	{
		window.close();
	}
}
