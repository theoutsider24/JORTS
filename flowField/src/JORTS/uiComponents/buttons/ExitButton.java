package JORTS.uiComponents.buttons;

import static JORTS.common.Constants.RESOLUTION_X;

import org.jsfml.graphics.Color;

import JORTS.core.GameWindow;

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
