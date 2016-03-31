package JORTS.common;

import java.awt.Toolkit;

import org.jsfml.graphics.Color;

public class Constants {
	public static boolean PAUSED=false;
	public static boolean SHOW_FLOW=false;
	public static boolean EDITING_MAP=false;
	
	public final static int CELL_SIZE=20;
	public final static int GRID_SIZE=100;
	
	public final static int STARTING_UNIT_COUNT=0;

	public final static String FONT="FreeMonoBold";

	public final static int RESOLUTION_X=1900;
	public final static int RESOLUTION_Y=1000;
	public final static boolean FULLSCREEN=false;
	public final static int FRAME_CAP=144;
	public final static float TARGET_DELTA_T=1000/FRAME_CAP;
	
	public final static String WINDOW_TITLE="JORTS";
	
	public final static String ROOT_DIRECTORY=System.getProperty("user.dir");
	public final static String MAP_DIRECTORY=ROOT_DIRECTORY+"//maps";
	public final static String LOG_DIRECTORY=ROOT_DIRECTORY+"//logs";
	public final static String FONT_DIRECTORY=ROOT_DIRECTORY+"//fonts";
	public final static String SOUND_DIRECTORY=ROOT_DIRECTORY+"//sounds";
	public final static String DEFINITIONS_DIRECTORY=ROOT_DIRECTORY+"//defs";
	
	public final static boolean MINIMAP_ON=false;
	
	public final static boolean SHOW_HEALTH_BARS=true;
	public final static boolean SHOW_VISION_MASK=false;
	
	public final static int COST_INCREASE_ON_CLICK=10;
	public final static int CAMERA_MOVEMENT_VALUE=8;
	
	public final static int LOWER_GUI_HEIGHT=240;
	public final static int UPPER_GUI_HEIGHT=30;
	public final static int BORDER_DRAG_TOLERANCE=10;
	
	public final static boolean MAP[][] = new boolean[GRID_SIZE][GRID_SIZE];
	
	public final static Color GROUND_COLOR = new Color(0,200,0);
	public final static Color OBSTACLE_COLOR = new Color(50,50,50);
	public final static int CLICK_INTERVAL = (Integer)Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");

	public final static boolean LOCK_ZOOM = false;
	public final static float MAX_ZOOM = 3;
	public final static float MIN_ZOOM = 1;
	public final static float ZOOM_VALUE=1.1f;
}
