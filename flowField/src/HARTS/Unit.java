package HARTS;

import org.apache.commons.math3.ml.clustering.DoublePoint;

public class Unit extends DoublePoint{
	String id;
	public String type;
	public Unit(String id,double x,double y)
	{
		super(new double[]{x,y});
		Thread t=new Thread(new Runnable(){
			@Override
			public void run()
			{
				type=Main.core.queryManager.getUnitType(id);
			}
		});
		t.start();
		System.out.println(type);
		this.id=id;
	}
}
