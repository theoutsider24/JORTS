package JORTS.gameElements.map;
import static JORTS.common.Constants.*;

import java.util.HashMap;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;

import JORTS.common.CommonFunctions;
import JORTS.core.Main;
import JORTS.core.Player;
import JORTS.gameElements.buildings.Building;
import JORTS.gameElements.map.VisionMap.Visibility;
import JORTS.gameElements.units.Entity;


public class VisionMap implements Drawable{
	UpdatableImage imageMask;
	enum Visibility{INVISIBLE,DARK,VISIBLE}
	public static HashMap<Visibility,Color> colors= new HashMap<Visibility,Color>();
	static
	{
		colors.put(Visibility.INVISIBLE,Color.BLACK);
		colors.put(Visibility.VISIBLE,Color.TRANSPARENT);
		colors.put(Visibility.DARK,new Color(0,0,0,80));
	}
	Visibility[][] grid = new Visibility[GRID_SIZE][GRID_SIZE];
	Player player;
	public VisionMap(Player p)
	{
		this.player=p;
		for(int i=0;i<GRID_SIZE;i++)
			for(int j=0;j<GRID_SIZE;j++)
			{
				grid[i][j]=Visibility.INVISIBLE;
			}
		
		imageMask = new UpdatableImage(new Vector2f(GRID_SIZE*CELL_SIZE,GRID_SIZE*CELL_SIZE),new Vector2f(GRID_SIZE,GRID_SIZE)){
			@Override
			public void updateImage()
			{
				for(int i=0;i<GRID_SIZE;i++)
					for(int j=0;j<GRID_SIZE;j++)						
						image.setPixel(i,j,getColorAt(i, j));
				updateTexture();
			}	
		};
		imageMask.texture.setSmooth(true);
	}
	public Color getColorAt(int x,int y)
	{
		return colors.get(grid[x][y]);
	}
	public boolean isVisible(int x,int y)
	{
		if(!SHOW_VISION_MASK)return true;
		return grid[x][y]==Visibility.VISIBLE;
	}
	public void update()
	{
		for(int i=0;i<GRID_SIZE;i++)
		{
			for(int j=0;j<GRID_SIZE;j++)
			{
				if(grid[i][j]==Visibility.VISIBLE)
					grid[i][j]=Visibility.DARK;
			}
		}
		for(Entity e:player.getUnits())
		{
			int visionRangePix=e.visionRange;
			int visionRangePixSqr=visionRangePix*visionRangePix;
			int visionRangeCells=(visionRangePix/CELL_SIZE)+2;
			
			Vector2f ePos = e.getPosition();
			MapCell cell = Main.worldMap.getCellAtPos(ePos);
			
			for(int i=cell.x-visionRangeCells;i<cell.x+visionRangeCells;i++)
				for(int j=cell.y-visionRangeCells;j<cell.y+visionRangeCells;j++)
				{
					if(i>=0&&j>=0&&i<GRID_SIZE&&j<GRID_SIZE)
					{
						if(!isVisible(i, j)&&CommonFunctions.getDistSqr(Main.worldMap.cells[i][j].getPosition(), e.getPosition())<visionRangePixSqr)
						{
							grid[i][j]=Visibility.VISIBLE;
						}
					}
				}
		}
		for(Building b:player.getBuildings())
		{
			int visionRangePix=b.visionRange;
			int visionRangePixSqr=visionRangePix*visionRangePix;
			int visionRangeCells=(visionRangePix/CELL_SIZE)+2;
			
			for(MapCell cell:b.occupiedCells)
			{
				for(int i=cell.x-visionRangeCells;i<cell.x+visionRangeCells;i++)
					for(int j=cell.y-visionRangeCells;j<cell.y+visionRangeCells;j++)
					{
						if(i>=0&&j>=0&&i<GRID_SIZE&&j<GRID_SIZE)
						{
							if(!isVisible(i, j)&&CommonFunctions.getDistSqr(Main.worldMap.cells[i][j].getPosition(), cell.getCenter())<visionRangePixSqr)
							{
								grid[i][j]=Visibility.VISIBLE;
							}
						}
					}
			}
		}
		imageMask.updateImage();
	}
	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) {
		arg0.draw(imageMask);		
	}
}
