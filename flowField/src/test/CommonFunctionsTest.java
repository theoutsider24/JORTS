package test;

import static org.junit.Assert.assertEquals;

import org.jsfml.graphics.RectangleShape;
import org.jsfml.system.Vector2f;
import org.junit.*;

import common.CommonFunctions;

public class CommonFunctionsTest {
	public static void main(String [] args)
	{
		normaliseVectorTest();
		RectangleShape rect = new RectangleShape();
		rect.setSize(new Vector2f(10,10));

		pointRectDistTest();
		getSectorTest( );
	}
	public static void normaliseVectorTest()
	{
		float testValues[][] = new float[][]{
				{3,4},
				{-3,-4},
				{3000,4000},
				{-3000,4000}
		};
		float testResults[][] = new float[][]{
			{.6f,.8f},
			{-.6f,-.8f},
			{.6f,.8f},
			{-.6f,.8f}
	};
		for(int i=0;i<testValues.length;i++)
			assertEquals(new Vector2f(testResults[i][0],testResults[i][1]),CommonFunctions.normaliseVector(new Vector2f(testValues[i][0],testValues[i][1])));
	}
	public static void pointRectDistTest( )
	{
		float pointInput[][] = new float[][]{
			{-1,-1},
			{11,-1},
			{11,11},
			{-1,11},
			
			{1,-1},
			{1,11},
			{-1,1},
			{11,1},
			
			{2,-2},
			{2,12},
			{-2,2},
			{12,2}
		};
		
		float rectInput[][] = new float[][]{
			{0,0,10},
			{0,0,10},
			{0,0,10},
			{0,0,10},
			
			{0,0,10},
			{0,0,10},
			{0,0,10},
			{0,0,10},
			
			{0,0,10},
			{0,0,10},
			{0,0,10},
			{0,0,10}
			
		};
		
		float results[] = new float[]{
			(float) Math.sqrt(2),
			(float) Math.sqrt(2),
			(float) Math.sqrt(2),
			(float) Math.sqrt(2),
			1,
			1,
			1,
			1,
			
			2,
			2,
			2,
			2
		};
		for(int i=0;i<results.length;i++)
		{
			Vector2f v = new Vector2f(pointInput[i][0],pointInput[i][1]);
			RectangleShape rect = new RectangleShape(new Vector2f(rectInput[i][2],rectInput[i][2]));
			rect.setPosition(rectInput[i][0],rectInput[i][1]);
			assertEquals(results[i],CommonFunctions.getDist(rect.getGlobalBounds(), v),0.00001);
			
		}
	}public static void getSectorTest( )
	{
		float pointInput[][] = new float[][]{
			{-1,-1},
			{11,-1},
			{11,11},
			{-1,11}
		};
		
		float rectInput[][] = new float[][]{
			{0,0,10},
			{0,0,10},
			{0,0,10},
			{0,0,10}
		};
		
		float results[] = new float[]{
			0,
			2,
			4,
			6
		};
		for(int i=0;i<results.length;i++)
		{
			Vector2f v = new Vector2f(pointInput[i][0],pointInput[i][1]);
			RectangleShape rect = new RectangleShape(new Vector2f(rectInput[i][2],rectInput[i][2]));
			rect.setPosition(rectInput[i][0],rectInput[i][1]);
			assertEquals(results[i],CommonFunctions.getSector(rect.getGlobalBounds(), v),0.00001);
			
		}
	}
}