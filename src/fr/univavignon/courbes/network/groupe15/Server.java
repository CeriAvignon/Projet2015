package fr.univavignon.courbes.network.groupe15;

import java.net.*;
import java.io.*;

import java.util.List;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.network.ServerCommunication;

/**
 * 
 * @author uapv1301073
 *
 */

public class Server implements ServerCommunication {
	final static int port = 3615;
	private Socket socket;
	/**
	 * @param args Port de connexion
	 * @throws IOException Gestion des exceptions
	 */
	public static void main(String[] args) throws IOException {
        
		try {
		      ServerSocket socketServeur = new ServerSocket(port);
		      System.out.println("Lancement du serveur");
		      while (true) {
		        Socket socketClient = socketServeur.accept();
		        Server Serveur = new Server(socketClient);
		        Serveur.run();
		      }
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
    }
	
	public Server(Socket socket) {
	    this.socket = socket;
	}

	  public void run() {
		  
		    try {
		      String message = "";

		      System.out.println("Connexion avec le client : " + socket.getInetAddress());

		      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		      message = in.readLine();
		      System.out.println(message);
		      if(message.contains("yolo"))
		    	  out.println("swag");
		      else
		    	  out.println("yolo?");
		      
		      socket.close();
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
	  }
	  

	@Override
	public String getIp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPort(int port) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void launchServer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeServer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendPlayers(List<Profile> profiles) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendPointThreshold(int pointThreshold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendBoard(Board board) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<Integer, Direction> retrieveCommands() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendText(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] retrieveText() {
		// TODO Auto-generated method stub
		return null;
	}

}
