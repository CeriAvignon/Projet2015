package fr.univavignon.courbes.network.groupe06;

import fr.univavignon.courbes.network.ServerCommunication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;

public class Server implements ServerCommunication {

	protected String ip;
	protected int port = 2345;
	protected boolean isRunning = false;
	protected static int size = 6;
	protected int nbClients = 0;
	protected String arrayOfIp[] = new String[size];
	protected static Socket socketArray[] = new Socket[size];
	protected BufferedInputStream reader = null;
	
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
		//step 1 : define address ip
		String adressIp;
 	    try {
 	        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
 	        while (interfaces.hasMoreElements()) {
 	            NetworkInterface iface = interfaces.nextElement();
 	            // filters out 127.0.0.1 and inactive interfaces
 	            if (iface.isLoopback() || !iface.isUp())
 	                continue;

 	            Enumeration<InetAddress> addresses = iface.getInetAddresses();
 	            while(addresses.hasMoreElements()) {
 	                InetAddress addr = addresses.nextElement();
 	                adressIp = addr.getHostAddress();
 	                if (adressIp.startsWith("192.168."))
 	                	this.ip = adressIp;
 	            }
 	        }
 	    } catch (SocketException e) {
 	        throw new RuntimeException(e);
 	    }
		//step 2 : define port if value by default is impossible
 	   try {
    		ServerSocket sSocket = new ServerSocket(this.port);
    	} catch (IOException e) {
        	this.port = 0;
    	}
 	   
 	    if(this.port == 0) {
 	    	for(int portTest = 1; portTest <= 3000; portTest++){
 	    		try {
 	    			ServerSocket sSocket = new ServerSocket(portTest);
 	        		this.port = portTest;
 	        		break;
 	    		} catch (IOException e) {
 	    			;
 	    		}
 	    	}
 	    }
		//step 3 : launch server
 	    Thread launch = new Thread(new Runnable(){
 	    	public void run(){
 	    		ServerSocket server = null;
 	    		isRunning = true;
 	    		while(isRunning == true){
 	    			try {
 	    				//wait client communication
 	    				Socket client = server.accept();			 
 	    				socketArray[nbClients] = client;
 	    				nbClients++;                 
 	    			} catch (IOException e) {
 	    				e.printStackTrace();
 	    			}
 	    		}
 	    		try {
 	    			server.close();
 	    		} catch (IOException e) {
 	    			e.printStackTrace();
 	    			server = null;
 	    		}
 	    	}
 	    });
	      
	   launch.start();
	}

	@Override
	public void closeServer() {
		this.isRunning = false;
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
					while(i < nbClients) { 
						InetAddress client = InetAddress.getByName(arrayOfIp[i]);
						DatagramPacket packet = new DatagramPacket(data, 4, client, port);
	        	      	socket.send(packet);
	        	      	// now send the Board
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
		
		//read();
		
		return null;
	}
	
	private String read() throws IOException {      
	      String response = "";
	      int stream;
	      byte[] b = new byte[4096];
	      stream = reader.read(b);
	      response = new String(b, 0, stream);      
	      return response;
	   }

	@Override
	public void sendProfiles(List<Profile> profiles) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Profile> retrieveProfiles() {
		// TODO Auto-generated method stub
		return null;
	}   
	
}
