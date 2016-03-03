package common;

import static common.Constants.MAP_DIRECTORY;

import java.awt.Toolkit;

import org.jsfml.graphics.Color;

public class Constants {
	public static boolean PAUSED=false;
	public final static int CELL_SIZE=100;
	public final static int GRID_SIZE=100;
	public final static int SECTOR_SIZE=10;
	
	public final static int STARTING_UNIT_COUNT=20;

	public final static String FONT="FreeMonoBold";

	public final static int RESOLUTION_X=1920;
	public final static int RESOLUTION_Y=1080;
	public final static boolean FULLSCREEN=false;
	public final static int FRAME_CAP=144;
	
	public final static String WINDOW_TITLE="JORTS";
	
	public final static String ROOT_DIRECTORY=System.getProperty("user.dir");
	public final static String MAP_DIRECTORY=ROOT_DIRECTORY+"//maps";
	public final static String LOG_DIRECTORY=ROOT_DIRECTORY+"//logs";
	public final static String FONT_DIRECTORY=ROOT_DIRECTORY+"//fonts";
	
	
	public final static String DEFAUL_MAP=MAP_DIRECTORY+"//FYP1.mp";
	
	public final static boolean SHOW_HEALTH_BARS=false;
	public final static boolean SHOW_VISION_MASK=true;
	
	public final static int COST_INCREASE_ON_CLICK=10;
	public final static float ZOOM_VALUE=1.5f;
	public final static int CAMERA_MOVEMENT_VALUE=10;
	
	public final static int LOWER_GUI_HEIGHT=240;
	public final static int UPPER_GUI_HEIGHT=30;
	public final static int BORDER_DRAG_TOLERANCE=40;
	
	public final static boolean MAP[][] = new boolean[GRID_SIZE][GRID_SIZE];
	
	public final static Color GROUND_COLOR = Color.GREEN;
	public final static Color OBSTACLE_COLOR = new Color(50,50,50);
	public final static int CLICK_INTERVAL = (Integer)Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");

}
