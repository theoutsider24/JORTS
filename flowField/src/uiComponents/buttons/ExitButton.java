package uiComponents.buttons;

import org.jsfml.graphics.Color;

import FYP.Main;

public class ExitButton extends SmallButton{

	public ExitButton(String title) {
		super("X");
		text.setCharacterSize(30);
		text.setOrigin(-2,10);
		setFillColor(Color.RED);
		setPosition(1890, 5);
	}
	@Override
	public void click()
	{
		Main.window.close();
	}
}
