package JORTS.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.plaf.InputMapUIResource;

import JORTS.core.Main;
import JORTS.core.Player;

public class Server extends Thread{
	 int portNumber=5555;
	 Player player;
	 boolean connected=false;
	 ServerSocket serverSocket;
     Socket clientSocket;
	 PrintWriter out;
     BufferedReader in;		
     QueryProcessor queryProcessor;
     EventManager eventManager;
     
     public void close()
     {
    	 try{serverSocket.close();}catch(Exception e){}
		  try{clientSocket.close();}catch(Exception e){}
		  try{out.close();}catch(Exception e){}
		  try{in.close();}catch(Exception e){}
     }
      @Override
      public void run()
      {
    	  try
    	  {
	    	  serverSocket = new ServerSocket(portNumber);
	          clientSocket = serverSocket.accept();
	    	  out = new PrintWriter(clientSocket.getOutputStream(), true);
	          in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));			  
	          queryProcessor = new QueryProcessor(this);
	    	  String inputLine="", outputLine="";
	    	  
	          while ((inputLine = in.readLine()) != null) 
	          {
	        	  if(inputLine!=null)
	        	  {
		        	  System.out.println("Client Says:"+inputLine);
		        	  inputLine=inputLine.toLowerCase();
		        	  if(inputLine.contains("name"))
		        	  {
		        		  String playerName = inputLine.substring(inputLine.lastIndexOf(":")+1);
		        		  boolean found=setPlayer(playerName);
		        		  if(found)out.println("Player "+playerName+" assigned");
		        		  else out.println("Player not found");
		        		  
		        	  }
		        	  else if(inputLine.contains("query"))
		        	  {
		        		  queryProcessor.processQuery(inputLine);
		        	  }
	        	  }
	          }
	      } 
    	  catch (IOException e) {
	          System.out.println("Exception caught when trying to listen on port "
	              + portNumber + " or listening for a connection");
	          System.out.println(e.getMessage());
	      }
    	  finally
    	  {
    		  close();
    	  }
	  }
      public void sendMessage(Player p,String s)
      {
    	  System.out.println(s);
    	  if(player!=null&&player==p)
    	  {
    		  System.out.println(s);
    		  out.println(s);
    	  }
      }
      public boolean setPlayer(String s)
      {
    	  for(Player p:Main.players)
		  {
			  System.out.println(p.name);
			  if(p.name.toLowerCase().equals(s))
			  {
				  this.player=p;
		    	  eventManager=new EventManager(player,this);
				  return true;
			  }
		  }
		  return false;
      }
}
