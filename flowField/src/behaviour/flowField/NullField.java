package behaviour.flowField;

import static common.Constants.GRID_SIZE;

import FYP.Main;
import gameElements.map.Map;

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
