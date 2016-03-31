package JORTS.behaviour.flowField;

import JORTS.core.Main;
import JORTS.gameElements.map.Map;

public class NullField extends Field{

	public NullField(Map worldMap) {
		super(worldMap);
	}
	public NullField() {
		super(Main.worldMap);
	}
	@Override
	public void recalculate()
	{
	}
	@Override
	public void update()
	{
	}
}
