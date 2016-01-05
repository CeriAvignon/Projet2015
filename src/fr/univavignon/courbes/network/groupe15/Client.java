package fr.univavignon.courbes.network.groupe15;

import java.net.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.network.ClientCommunication;

/**
 * 
 * @author uapv1504059
 *
 */

public class Client implements ClientCommunication {

	/** Port du serveur */
	private int port;
	/** Adresse ip du serveur */
	private String ip;
	/** Connecteur côté client */
	private Socket socket;
	
	private Board board;

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
			this.socket = new Socket(ip, port);
			if(socket == null) {
    			System.out.println("Connexion échouée...");
    		} else {
    			System.out.println("Connexion établie !!");
    		}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void closeClient() {
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String retrieveText() {
		// TODO be not blocking
		try {
			String message = "";
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			message = in.readLine();
			return message;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void sendText(String message) {
		// TODO be not blocking
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    		out.println(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Profile> retrieveProfiles() {
		// TODO be non blocking
		try{
			System.out.println("YOLOOOOOOOOOO !!!!!");
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			List<Profile> lp = (List<Profile>)ois.readObject();
			return lp;
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Integer retrievePointThreshold() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Board retrieveBoard() {
		// TODO be non blocking
		Thread t = new Thread(new Runnable(){
			@Override
			public void run(){
				try{
					System.out.println("YOLOOOOOOOOOO !!!!!");
					ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
					Board b = (Board)ois.readObject();
					board = b;
				} catch(Exception e){
					board = null;
					e.printStackTrace();
				}
			}
		});
		t.start();
		return board;
	}

	@Override
	public void sendCommands(Map<Integer, Direction> commands) {
		// TODO Auto-generated method stub
		
	}

}
