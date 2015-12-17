package fr.univavignon.courbes.network.groupe05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
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
	
	/** Port du serveur */
	private int port;
	/** Connecteur Serveur/Clients */
	private ArrayList<Socket> sockets;
	/** Connecteur côté Serveur */
	private ServerSocket serverSocket;
	
	String[] messages = { "" };
	String messageSent;
	
	/**
	 * @param sockets Connecteur Serveur/Clients
	 */
	/*public Server(Socket socket) {
	    this.socket = socket;
	}*/

	@Override
	public String getIp() {
		InetAddress IP;
		try {
			IP = InetAddress.getLocalHost();
			return IP.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int getPort() {
		return this.port;
	}

	@Override
	public void setPort(int port) {
		this.port = port;
	}
	
	@Override
	public void launchServer() {
		try {
			this.serverSocket = new ServerSocket(port);
			System.out.println("Lancement du serveur");
			new Thread(new Runnable(){
				public void run(){
					try {
						sockets.add(serverSocket.accept());
						System.out.println("Connexion avec le client : " + sockets.get(sockets.size()-1).getInetAddress());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			messageSent = this.retrieveText()[0];
			this.sendText(messageSent);
			
			/*while (true) {
				Socket socketClient = serverSocket.accept();
				Server server = new Server(socketClient);
				System.out.println("Connexion avec le client : " + sockets.getInetAddress());
				String[] s = server.retrieveText();
				sendText(s[0]);
				
			}*/
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}

	@Override
	public void closeServer() {
		try {
			for(Socket socket:sockets){
				socket.close();
			}
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendText(String message) {
		messageSent = message;
		new Thread(new Runnable(){
			public void run(){
				try {
					for(Socket socket:sockets){
						PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
						out.println(messageSent);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public String[] retrieveText() {
		new Thread(new Runnable(){
			public void run(){
				try {
					int i = 0;
					for(Socket socket:sockets){
						BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						messages[i] = in.readLine();
						System.out.println(messages[i]);
						i++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return messages;
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

}
