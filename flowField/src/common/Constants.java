package common;

import java.awt.Toolkit;

import org.jsfml.graphics.Color;

public class Constants {
	public final static int CELL_SIZE=100;
	public final static int GRID_SIZE=100;
	public final static int SECTOR_SIZE=10;
	
	public final static int STARTING_UNIT_COUNT=100;

	public final static String FONT="FreeMonoBold";

	public final static int RESOLUTION_X=1920;
	public final static int RESOLUTION_Y=1080;
	public final static boolean FULLSCREEN=false;
	public final static int FRAME_CAP=60;
	
	public final static String WINDOW_TITLE="Flow Field Demo";
	public final static String DEFAUL_MAP="FYP1.mp";

	public final static boolean SHOW_HEALTH_BARS=true;
	
	public final static int COST_INCREASE_ON_CLICK=10;
	public final static float ZOOM_VALUE=1.5f;
	public final static int CAMERA_MOVEMENT_VALUE=10;
	
	public final static int LOWER_GUI_HEIGHT=240;
	public final static int UPPER_GUI_HEIGHT=30;
	
	public final static boolean MAP[][] = new boolean[GRID_SIZE][GRID_SIZE];
	
	public final static Color GROUND_COLOR = Color.GREEN;
	public final static Color OBSTACLE_COLOR = new Color(50,50,50);
	public final static int CLICK_INTERVAL = (Integer)Toolkit.getDefaultToolkit().
	        getDesktopProperty("awt.multiClickInterval");

}
