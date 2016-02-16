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
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ErrorHandler;
import fr.univavignon.courbes.inter.ServerProfileHandler;

/**
 * @author TORO Loïc
 *
 *Classe fille de ServerCommunication, elle en implémente toutes les méthodes.
 */
public class Server implements ServerCommunication {

	/** Variable qui contient l'adresse ip du serveur */
	protected String ip;
	/** Variable qui contient le port du serveur */
	protected int port = 2345;
	/**Variable qui contient permet au serveur d'attendre des connexions clients tant qu'il est ouvert. */
	protected boolean isRunning = false;
	/**Variable qui définit le nombre maximum de connexion que le serveur peut accueillir. */
	protected static int size = 6;
	/** Variable qui gére le nombre de clients connectés */
	protected static int nbClients = 0;
	/** Liste qui contient toute les adresses ip des clients pour l'envoie en UDP */
	protected ArrayList<String> arrayOfIp = new ArrayList<String>();
	/** Liste qui contient toute les sockets pour l'envoie en TCP  */
	protected ArrayList<Socket> socketArray = new ArrayList<Socket>();
	/** Socket du serveur. */
	private ServerSocket sSocket = null;
	/** Buffer pour les commandes d'un client */
	protected Map<Integer, Direction> commands= null;
	/** Erreur de profil */
	protected ServerProfileHandler profileHandler;
	/**Envoie d'un message d'erreur a l'IU	 */
	protected ErrorHandler messageError;
	
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
		System.setProperty("java.net.preferIPv4Stack" , "true");
 	    try {
 	        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
 	        while (interfaces.hasMoreElements()) {
 	        	NetworkInterface iface = interfaces.nextElement();
 	            // filters out 127.0.0.1 and inactive interfaces
 	            if (iface.isLoopback() || !iface.isUp())
 	                continue;

 	            Enumeration<InetAddress> addresses = iface.getInetAddresses();
 	            InetAddress addr = addresses.nextElement();
 	            adressIp = addr.getHostAddress();
 	            this.ip = adressIp;
 	            System.out.println(this.ip);
 	        }
 	    } catch (SocketException e) {
 	        throw new RuntimeException(e);
 	    }
		//step 2 : define port if value by default is impossible
 	   try {
    		this.sSocket = new ServerSocket(this.port);
    	} catch (IOException e) {
        	this.port = 0;
    	}
 	   
 	    if(this.port == 0) {
 	    	for(int portTest = 1; portTest <= 6000; portTest++){ // find a free port
 	    		try {
 	    			this.sSocket = new ServerSocket(portTest);
 	        		this.port = portTest;
 	        		break;
 	    		} catch (IOException e) {
 	    			; //error here is normal , so we don't need an action.
 	    		}
 	    	}
 	    }
		//step 3 : launch server
 	   
		try {
			sSocket.close();
			sSocket = new ServerSocket(this.port, Server.size, InetAddress.getByName(this.ip));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
 	    Thread launch = new Thread(new Runnable(){
 	    	@Override
			public void run(){
 	    		isRunning = true;
 	    		while(isRunning){
 	    			try {
 	    				//wait client communication
 	    				if (nbClients < size){
	 	    				Socket client = sSocket.accept(); 
	 	    				socketArray.add(client);
	 	    				arrayOfIp.add(client.getInetAddress().getHostAddress());
	 	    				nbClients++;
	 	    				Thread newClient = new Thread(new ClientProcessor(client));
	 	    				newClient.start();
 	    				}
 	    			} catch (IOException e) {
 	    				e.printStackTrace();
 	    			}
 	    		}
 	    		try {
 	    			sSocket.close();
 	    		} catch (IOException e) {
 	    			e.printStackTrace();
 	    			sSocket = null;
 	    		}
 	    	}
 	    });  
	   launch.start();
	}

	@Override
	public void closeServer() {
		this.isRunning = false;
		sendText("/close");
		try {
			sSocket.close();
		} catch (IOException e) {
			sSocket=null;
			e.printStackTrace();
		}
	}

	@Override
	public void sendPointThreshold(final int pointThreshold) {
		Thread send = new Thread(new Runnable(){
			@Override
			public void run(){
				sendObject(pointThreshold);
			}
		});
		send.start();
		return;
	}

	@Override
	public void sendBoard(final Board board) {
		Thread send = new Thread(new Runnable(){
			@Override
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
					
					for(String adresseIp:arrayOfIp){ 
						InetAddress client = InetAddress.getByName(adresseIp);
						DatagramPacket packet = new DatagramPacket(data, 4, client, port+1);
	        	      	socket.send(packet);
	        	      	// now send the Board
	        	      	packet = new DatagramPacket(Buf, Buf.length, client, port+1);
	        	      	socket.send(packet);
					}
					socket.close();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
		send.start();
	}

	@Override
	public Map<Integer, Direction> retrieveCommands() {
		return this.commands;
	}

	/**
	 * Envoie un message à tous les clients.
	 * @param message
	 * 		Un String contenant le message a envoyé.
	 */
	public void sendText(final String message) {
		Thread send = new Thread(new Runnable(){
			@Override
			public void run(){
				sendObject(message);
			}
		});
		send.start();
		return ;
	}
	
	@Override
	public void sendProfiles(final List<Profile> profiles) {
		Thread send = new Thread(new Runnable(){
			@Override
			public void run(){
				sendObject(profiles);
			}
		});
		send.start();
		return;
	}
	
	/**
	 * 	Permet d'envoyer un objet a tous les clients.
	 * @param objet
	 * 		Correspond à n'importe quel objet à sérialiser puis à envoyer.
	 */
	private synchronized void sendObject(Object objet) {
		for(Socket sock:socketArray){
			try {
				ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
				oos.writeObject(objet);
				oos.flush();
			
			} catch (IOException e) {
				nbClients--;
				socketArray.remove(sock);
				arrayOfIp.remove(sock.getInetAddress().getHostAddress());
				messageError.displayError("L'ip "+sock.getInetAddress().getHostAddress()+" a été déconnecté.");
				try {
					sock.close();
				} catch (IOException e1) {
					sock = null;
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		return;
	}

	@Override
	public void setErrorHandler(ErrorHandler errorHandler) {
		this.messageError = errorHandler;
	}

	@Override
	public void setProfileHandler(ServerProfileHandler profileHandler) {
		this.profileHandler = profileHandler;
	}
	
}
