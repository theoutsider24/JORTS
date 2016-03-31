package HARTS;

import org.apache.commons.math3.ml.clustering.DoublePoint;

public class Building  extends DoublePoint{
	public String id;
	public String type;
	public Building(String id,double x,double y)
	{
		super(new double[]{x,y});
		Thread t=new Thread(new Runnable(){
			@Override
			public void run()
			{
				type=Main.core.queryManager.getBuildingType(id);
			}
		});
		t.start();
		this.id=id;
	}
}