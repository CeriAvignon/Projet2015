package fr.univavignon.courbes.network.groupe06;

import fr.univavignon.courbes.network.ClientCommunication;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;

/**
 * @author Loïc
 *
 */
public class Client implements ClientCommunication {

	/**
	 * Variable qui contient l'adresse ip du serveur
	 */
	protected String ip;
	/**
	 * Variable qui contient le port du serveur
	 */
	protected int port = 2345;
	/**
	 * Socket du client connecté au serveur
	 */
	protected Socket connexion = null;
	/**
	 * Buffer pour le board 
	 */
	protected Board board = null;

	@Override
	public String getIp() {
		
		return this.ip;
	}

	@Override
	public void setIp(String ip) {
		
		this.ip = ip;
		
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
	public void launchClient() {
		try {
			
			this.connexion = new Socket(ip, port);
			this.threadRetrieveBoard();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void closeClient() {
		try {
			//envoyer message au serveur pour prévenir.
			this.connexion.close();
			this.connexion = null;
		} catch (IOException e){
			e.printStackTrace();
		}
		
	}

	@Override
	public List<Profile> retrieveProfiles() {
		
		return null;
	}

	@Override
	public Integer retrievePointThreshold() {

		return null;
	}

	@Override
	public Board retrieveBoard() {
		return this.board;
	}
	
	/**
	 * Permet de constament recevoir un objet board 
	 * et de le stocker dans la variable de classe.
	 */
	public void threadRetrieveBoard()
	{
		Thread retrieve = new Thread(new Runnable(){
			@Override
			public void run(){
				while(connexion != null)
				{
					try {
						DatagramSocket socket = new DatagramSocket(port+1);

						byte[] data = new byte[4];
						DatagramPacket packet = new DatagramPacket(data, data.length );
						socket.receive(packet);

						int len = 0;
						// byte[] -> int
						for (int i = 0; i < 4; ++i) {
							len |= (data[3-i] & 0xff) << (i << 3);
						}

						// now we know the length of the payload
						byte[] buffer = new byte[len];
						packet = new DatagramPacket(buffer, buffer.length );
						socket.receive(packet);

						ByteArrayInputStream baos = new ByteArrayInputStream(buffer);
						ObjectInputStream oos = new ObjectInputStream(baos);
						board = (Board)oos.readObject();
						socket.close();
				    
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		retrieve.start();
		
	}
	@Override
	public void sendCommands(Map<Integer, Direction> commands) {
		return;
	}

	@Override
	public String retrieveText() {
		return null;
	}

	@Override
	public void sendText(final String message) {
		Thread send = new Thread(new Runnable(){
			@Override
			public void run(){
				sendTextSecure(message);
			}
		});
		send.start();
		return;
		
	}
	/**
	 * @param message 
	 * 		Le message a envoyer.
	 */
	synchronized public void sendTextSecure(String message)
	{
		Socket sock = connexion;
		if(connexion != null) {
			try {
				PrintWriter writer = new PrintWriter(sock.getOutputStream());
				writer.write(message);
				writer.flush();
				writer = null;
			} catch(SocketException e){
				closeClient();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		sock = null;
		
	}
	
	
	@Override
	public void sendProfile(Profile profile) {
		return;
		
	}

}
