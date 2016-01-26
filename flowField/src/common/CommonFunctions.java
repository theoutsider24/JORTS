package common;

import static common.Constants.*;
import java.util.ArrayList;

import org.jsfml.graphics.CircleShape;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import FYP.Main;


public class CommonFunctions {
	public static float correctAngle(float a2)
	{
		while(a2<0)
			a2+=360;
		while(a2>=360)
			a2-=360;
		return a2;
	}
	public static boolean isAngleInRange(float a1, float a2,float a3)
	{
		
		a1 = correctAngle(a1);
		a2 = correctAngle(a2);
		a3 = correctAngle(a3);
		if(a2<a3)//left to right
		{
		//	System.out.println("first");
			return a1>a2&&a1<a3;
		}
		else
		{
			if(a1>180)
				a3+=360;
			else
				a2-=360;
			//System.out.println("second");
			return a1>a2&&a1<a3;
		}
	}
	
	public static boolean isInRange(Vector2f p1,Vector2f p2, int dist)
	{
		float xDiff =  (p1.x-p2.x);
		float yDiff = (p1.y-p2.y);
		//if()
		float distSqr = (xDiff*xDiff) + (yDiff*yDiff);
		return distSqr<(dist*dist);
	}
	public static double getDistSqr(Vector2f p1,Vector2f p2)
	{
		float xDiff = p1.x-p2.x;
		float yDiff = p1.y-p2.y;
		float distSqr = (xDiff*xDiff) + (yDiff*yDiff);
		return distSqr;
	}
	public static double getDist(Vector2f p1,Vector2f p2)
	{
		return Math.sqrt(getDistSqr(p1,p2));
	}
	public static double getDist(FloatRect rect,Vector2f p2)
	{
		double dist=0;
		//8 possibilities
		int sector = getSector(rect,p2);
		
		switch(sector)
		{
			case 0: dist = getDist(p2,new Vector2f(rect.left,rect.top)); break;
			case 1: dist = p2.y-rect.top;  break;
			case 2: dist = getDist(p2,new Vector2f(rect.left+rect.width,rect.top)); break;
			case 3: dist = p2.x-(rect.left+ rect.width); break;
			case 4: dist = getDist(p2,new Vector2f(rect.left+rect.width,rect.top+rect.height)); break;
			case 5: dist = p2.y-(rect.top+ rect.height); break;
			case 6: dist = getDist(p2,new Vector2f(rect.left,rect.top+rect.height)); break;
			case 7: dist = p2.x-rect.left; break;
		}		
		return Math.abs(dist);
	}
	/*public static double getDist(FloatRect rect1,FloatRect rect2)
	{
		if(Math.abs(rect1.top-rect2.top)<rect1.height+rect2.height)
			
	}*/
	public static int getSector(FloatRect rect,Vector2f p2)
	{
		final int left =0;
		final int right=2;
		final int top=0;
		final int bot =2;
		final int center=1;
		
		int[] pos = new int[2];
		
		if(p2.x<rect.left)
			pos[0]=left;
		else if(p2.x<=rect.left+rect.width)
			pos[0]=center;
		else
			pos[0]=right;
		
		if(p2.y<rect.top)
			pos[1]=top;
		else if(p2.y<=rect.top+rect.height)
			pos[1]=center;
		else
			pos[1]=bot;
		
		switch(pos[0])
		{
			case left: 
				switch(pos[1])
				{
					case top: return 0; 
					case center: return 7;
					case bot: return 6;
				}
				break; 
				
			case center:  
				switch(pos[1])
				{
					case top: return 1;
					case center: return 9;
					case bot:return 5;
				}
				break; 
			case right:  
				switch(pos[1])
				{
					case top: return 2;
					case center: return 3; 
					case bot: return 4;
				}
				break; 
		}
		return 10;
	}
	public static float getAngle(Vector2f p1,Vector2f p2)
	{
		float xDiff =  (p1.x-p2.x);
		float yDiff = (p1.y-p2.y);
		float angle = (float) (Math.toDegrees(Math.atan2(yDiff, xDiff))-90);
		return CommonFunctions.correctAngle(angle);
	}
	public static float getAngleRads(Vector2f p1,Vector2f p2)
	{
		float xDiff =  (p1.x-p2.x);
		float yDiff = (p1.y-p2.y);
		return (float) (Math.atan2(yDiff, xDiff));
		
	}
	public static Vector2f rotateVectorDeg(Vector2f v, Vector2f centre, float a)
	{
		return rotateVectorRads(v,centre, Math.toRadians(a));
	}
	public static Vector2f rotateVectorRads(Vector2f v, Vector2f c, double rads)
	{
		Vector2f temp = new Vector2f(v.x-c.x,v.y-c.y);
		float newX=(float) ( (temp.x * Math.cos(rads)) -  (temp.y * Math.sin(rads)));
		float newY=(float) ( (temp.x * Math.sin(rads)) +  (temp.y* Math.cos(rads)));
		return new Vector2f(newX+c.x,newY+c.y);
	}
	public static Vector2f normaliseVector(Vector2f v1)
	{
		float len = getLength(v1);
		if(len!=0)
			return Vector2f.div(v1, len);
		else
			return v1;
	}
	public static float getAngleBetweenVectors(Vector2f p1, Vector2f p2)
	{
		float result = (float) Math.atan2(p1.y - p2.y, p1.x - p2.x);
		/*if(result<0)
		{
			result+=Math.PI;
		}*/
		return result;
	}
	public static float getLength(Vector2f v)
	{
		return  (float) getDist(v,new Vector2f(0,0));
	}
	public static ArrayList<int[]> getBresenhamLine(int x1,int y1,int x2,int y2)
	{
		ArrayList<int[]> points = new ArrayList<int[]>();
		int dx = Math.abs(x2 - x1);
		int dy = Math.abs(y2 - y1);

		int sx = (x1 < x2) ? 1 : -1;
		int sy = (y1 < y2) ? 1 : -1;

		int err = dx - dy;

		while (true) {
			points.add(new int[]{x1,y1});
		  //  getCell(x1, y1).close();

		    if (x1 == x2 && y1 == y2) {
		        break;
		    }

		    int e2 = 2 * err;

		    if (e2 > -dy) {
		        err = err - dy;
		        x1 = x1 + sx;
		    }

		    if (e2 < dx) {
		        err = err + dx;
		        y1 = y1 + sy;
		    }
		}
		return points;
	}
	public static Vector2f limitVectorLength(Vector2f v,float maxLength)
	{
		float len = getLength(v);
		if(len>maxLength)
		{
			v=normaliseVector(v);
			v=Vector2f.mul(v,maxLength);
		}
		//System.out.printf("%f\n",getLength(v));
		return v;
	}
	public static boolean contains(CircleShape c,Vector2f v)
	{
		return getDistSqr(c.getPosition(),v)<=(c.getRadius()*c.getRadius());
	}
	public static boolean isOnScreen(RectangleShape r)
	{
		Main.window.setView(Main.gameView);
		Vector2i screenPos = Main.window.mapCoordsToPixel(r.getPosition());
		if(screenPos.x<-10||screenPos.y<-10||screenPos.x>RESOLUTION_X+10||screenPos.y>RESOLUTION_Y+10)
			return false;
		return true;
		
	}
}


