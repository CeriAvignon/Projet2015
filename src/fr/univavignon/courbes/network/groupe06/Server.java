package fr.univavignon.courbes.network.groupe06;

import fr.univavignon.courbes.network.ServerCommunication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;

public class Server implements ServerCommunication {

	protected String ip;
	protected int port;
	protected int size=6;
	protected int nbConnections=0;
	protected String arrayOfIp[] = new String[size];
	
	@Override
	public String getIp() {
		
		return this.ip;
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
	public void sendBoard(final Board board) {
		Thread send = new Thread(new Runnable(){
			public void run(){
				try {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(baos);
					oos.writeObject(board);
					oos.flush();
					// get the byte array of the object
					byte[] Buf= baos.toByteArray();
					
					int number = Buf.length;;
					byte[] data = new byte[4];
					
					// int -> byte[]
					for (int i = 0; i < 4; ++i) {
						int shift = i << 3; // i * 8
						data[3-i] = (byte)((number & (0xff << shift)) >>> shift);
					}
					DatagramSocket socket = new DatagramSocket(port);
					int i=0;
					while(i < nbConnections) {// i < taille du tableau qui contient les ip des clients 
						InetAddress client = InetAddress.getByName(arrayOfIp[i]);
						DatagramPacket packet = new DatagramPacket(data, 4, client, port);
	        	      	socket.send(packet);
	        	      	// now send the payload
	        	      	packet = new DatagramPacket(Buf, Buf.length, client, port);
	        	      	socket.send(packet);
	        	      	i++;
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
		send.start();
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
