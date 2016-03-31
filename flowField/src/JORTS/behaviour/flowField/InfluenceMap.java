package JORTS.behaviour.flowField;

import static JORTS.common.Constants.CELL_SIZE;
import static JORTS.common.Constants.GRID_SIZE;

import org.jsfml.system.Vector2f;

import JORTS.common.CommonFunctions;
public class InfluenceMap {
	public int[][] costs = new int[GRID_SIZE][GRID_SIZE];
	public InfluenceMap(Vector2f pos,float range)
	{
		for(int i=0;i<GRID_SIZE;i++)
		{
			for(int j=0;j<GRID_SIZE;j++)
			{
				Vector2f center = new Vector2f((i*CELL_SIZE )+(CELL_SIZE/2), (j*CELL_SIZE )+(CELL_SIZE/2));
				double dist = CommonFunctions.getDist(center,pos);
				costs[i][j] = (int)(dist/CELL_SIZE)*2;
			}
		}
	}
	public InfluenceMap(Vector2f pos,float range,float rad)
	{
		
	}
}
