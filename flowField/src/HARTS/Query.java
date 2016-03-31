package HARTS;

public class Query extends Thread{
	static int numOfQueries=0;
	public String id;
	public String query;
	public String response;
	public Query(String query)
	{
		id="query#"+numOfQueries++;
		this.query=query;
		start();
	}
	@Override
	public void run()
	{
		Main.core.sendMessage(id+":"+query);
		try {
			synchronized (this) {
				wait(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void setResponse(String s)
	{
		response=s.replace(id+":", "");
		synchronized (this) {
			notify();
		}
	}
}
	
