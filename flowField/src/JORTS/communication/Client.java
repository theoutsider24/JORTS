package JORTS.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import HARTS.EventListener;
import HARTS.Main;
import JORTS.core.Player;

public class Client extends Thread{
	Socket socket;
    PrintWriter out;
    BufferedReader in;
    public String reports="";
    String playerName="";
    EventListener listener;
    public boolean ready=false;
    public Client(String pName)
    {
    	playerName=pName;
    }
    public void setEventListener(EventListener eL)
    {
    	listener=eL;
    }
	 public void run()
	 {
        try 
        {
            socket = new Socket("localhost", 5555);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String fromServer;
            out.println("This is my name:"+playerName);
            while ((fromServer = in.readLine()) != null) 
            {
                if(listener!=null)
            	{
                	ready=true;
                	listener.reportEvent(fromServer);
            	}
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host localhost" );
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to localhost");
        }
        finally
        {
        	try{socket.close();}catch(IOException ex){}
        	out.close();
        	try{in.close();}catch(IOException ex){}
        }
    }
	 public void sendMessage(String s)
     {
   		  out.println(s);
     }
	 public boolean isReady()
	 {
		 return out!=null;
	 }
	 public String retrieveReports()
	 {
		 String value="";
		 value+=reports.trim();
		 reports="";
		 return value;
	 }
}
