package fr.univavignon.courbes.network.groupe15;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ClientProfileHandler;
import fr.univavignon.courbes.inter.ErrorHandler;
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

	/*@Override
	public String retrieveText() {
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
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    		out.println(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/

	@Override
	public Integer retrievePointThreshold() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Board retrieveBoard() {
		// TODO Auto-generated method stub
		try{
			System.out.println("YOLOOOOOOOOOO !!!!!");
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			Board b = (Board)ois.readObject();
			return b;
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void sendCommands(Map<Integer, Direction> commands) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setErrorHandler(ErrorHandler errorHandler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setProfileHandler(ClientProfileHandler profileHandler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean addProfile(Profile profile) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeProfile(Profile profile) {
		// TODO Auto-generated method stub
		
	}

}