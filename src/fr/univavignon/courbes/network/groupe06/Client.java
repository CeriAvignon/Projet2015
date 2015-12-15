package fr.univavignon.courbes.network.groupe06;

import fr.univavignon.courbes.network.ClientCommunication;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;

public class Client implements ClientCommunication {

	protected String ip;
	protected int port = 2345;
	protected Socket connexion = null;
	protected Board board = new Board();
	
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
		} catch (IOException e){
			e.printStackTrace();
		}
		
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
		
		Thread retrieve = new Thread(new Runnable(){
			public void run(){
				try {
					DatagramSocket socket = new DatagramSocket(port);

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
				    
				} catch(Exception e) {
				    e.printStackTrace();
				}
			}
		});
		retrieve.start();
		return this.board;
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

	@Override
	public void sendProfile(Profile profile) {
		// TODO Auto-generated method stub
		
	}

}
