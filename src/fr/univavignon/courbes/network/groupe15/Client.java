package fr.univavignon.courbes.network.groupe15;

import java.net.*;
import java.io.*;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
	private static final int WAITING = 0;
    private static final int SENTKNOCKKNOCK = 1;
    private static final int SENTCLUE = 2;
    private static final int ANOTHER = 3;

    private static final int NUMJOKES = 5;

    private int state = WAITING;
    private int currentJoke = 0;

    private String[] clues = { "Turnip", "Little Old Lady", "Atch", "Who", "Who" };
    private String[] answers = { "Turnip the heat, it's cold in here!",
                                 "I didn't know you could yodel!",
                                 "Bless you!",
                                 "Is there an owl in here?",
                                 "Is there an echo in here?" };
    private Socket socket = null;

    /**
     * 
     * @param ip The ip of the distant server
     * @param port The port used to connect
     */
    public void initConnection(String ip, int port) {
    	try{
    		socket = new Socket(ip, port);
    		if(socket == null){
    			System.out.println("FAIL !!!!!!!");
    		} else {
    			System.out.println("YOLO SWAG REUSSI !");
    		}
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			Scanner sc = new Scanner(System.in);
    		out.println(sc.nextLine());
    		System.out.println(in.readLine());
    	}catch (Exception e) {
    		System.out.println("Catched !");
    	}
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	@Override
	public String getIp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIp(String ip) {
		// TODO Auto-generated method stub
		
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
	public void launchClient() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeClient() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Profile> retrieveProfiles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer retrievePointThreshold() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Board retrieveBoard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendCommands(Map<Integer, Direction> commands) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String retrieveText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendText(String message) {
		// TODO Auto-generated method stub
		
	}

}
